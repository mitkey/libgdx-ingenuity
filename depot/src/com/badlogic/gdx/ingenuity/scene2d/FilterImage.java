package com.badlogic.gdx.ingenuity.scene2d;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.StreamUtils;

/**
 * @作者 mitkey
 * @时间 2017年5月19日 下午6:38:19
 * @类说明 FilterImage.java <br/>
 *      来自 @see <a href="https://www.ayocrazy.com/?p=142">https://www.ayocrazy.com/?p=142</a> <br>
 * 
 *      <pre>
 *      // 无滤镜
 *      img1.setType(FilterImage.FilterType.none);
 *      // 高斯模糊
 *      img2.setType(FilterImage.FilterType.gaussian);
 *      img2.setRadius(2);
 *      img2.setSigma(1.5f);
 *      // 均值模糊
 *      img3.setType(FilterImage.FilterType.median);
 *      img3.setRadius(2);
 *      // 锐化
 *      img4.setType(FilterImage.FilterType.sharpen);
 *      img3.setRadius(2);
 *      // 拉普拉斯边缘检测
 *      img5.setType(FilterImage.FilterType.laplacian);
 *      img5.setRadius(2);
 *      // 膨化
 *      img6.setType(FilterImage.FilterType.dilate);
 *      img6.setRadius(2);
 *      // 腐蚀
 *      img7.setType(FilterImage.FilterType.erode);
 *      img7.setRadius(2);
 *      // 灰度
 *      img8.setType(FilterImage.FilterType.gray);
 *      // 泛黄
 *      img9.setType(FilterImage.FilterType.sepia);
 *      img9.setSepiaTone(1.3f, 1f, 0.7f);
 *      // 反色
 *      img10.setType(FilterImage.FilterType.negative);
 *      </pre>
 * 
 * 
 *      {@link #setType(FilterType)} 方法可以设置滤镜类型，sigma，radius，sepiaTone三个参数都可以通过set方法设置，实际上只有高斯模糊会用到sigma，只有泛黄会用到sepiaTone，radius参数大多滤镜都会用到。
 * @版本 0.0.1
 */
public class FilterImage extends Image {
	static ShaderProgram filterShader;

	/** 权重数组 */
	private float weights[];
	/** 模糊半径 */
	private int radius = 2;
	/** 高斯分布方差值 */
	private float sigma = 1f;
	/** sepia特效rgb比例 */
	private Vector3 sepiaTone = new Vector3(1, 1, 1);

	private FilterType type = FilterType.none;
	private boolean updateWeights;

	public FilterImage(Texture texture) {
		super(texture);
		if (filterShader == null) {
			try {
				filterShader = createShader();
			} catch (IOException e) {
				Gdx.app.error(FilterImage.class.getName(), "create shader fail", e);
			}
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		// 这里记录batch之前使用的shader
		ShaderProgram shader = batch.getShader();
		// 使用我们自定义的Shader
		batch.setShader(filterShader);
		// 分别设置权重数组，模糊半径和纹理size
		setUniform();
		// 绘制
		super.draw(batch, parentAlpha);
		// 将batch设置回之前的shader，否则它将继续使用我们自定义的灰度算法Shader
		batch.setShader(shader);
	}

	/** 模糊半径 */
	public void setRadius(int radius) {
		this.radius = radius;
		updateWeights = true;
	}

	/** sepia特效rgb比例 */
	public void setSepiaTone(float r, float g, float b) {
		this.sepiaTone = new Vector3(r, g, b);
	}

	/** 高斯分布方差值 */
	public void setSigma(float sigma) {
		this.sigma = sigma;
		updateWeights = true;
	}

	public void setType(FilterType type) {
		this.type = type;
		updateWeights = true;
	}

	private void calculateWeights() {
		if (type.name != 1)
			return;
		weights = new float[(radius * 2 + 1) * (radius * 2 + 1)];
		int index = 0;
		float totalWeights = 0f;
		for (int i = -radius; i <= radius; i++) {
			for (int j = -radius; j <= radius; j++) {
				float value = getWeight(new Vector2(i, j));
				totalWeights += value;
				weights[index++] = value;
			}
		}
		if (type == FilterType.gaussian || type == FilterType.median) {
			// 保证权重总和为1，拉普拉斯和锐化不需要
			for (int i = 0; i < weights.length; i++) {
				weights[i] /= totalWeights;
			}
		}
		updateWeights = false;
	}

	private ShaderProgram createShader() throws IOException {
		String vertexShader = StreamUtils.copyStreamToString(FilterImage.class.getResource("filter.vert").openStream());
		String fragmentShader = StreamUtils.copyStreamToString(FilterImage.class.getResource("filter.frag").openStream());
		ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
		if (shader.isCompiled() == false)
			throw new IllegalArgumentException("Error compiling filterShader: " + shader.getLog());
		return shader;
	}

	// 根据类型不同，计算某一点的权重
	private float getWeight(Vector2 v) {
		float value = 0;
		switch (type) {
			case gaussian:
				// 二维高斯分布公式
				double result = 1f / (2 * Math.PI * sigma * sigma) * Math.pow(Math.E, -v.dst2(0, 0) / (2 * sigma * sigma));
				value = (float) result;
				break;
			case median:
				// 均值模糊权限全为1
				value = 1f;
				break;
			case sharpen:
				// 锐化
				if (v.x == 0 && v.y == 0)
					value = (radius * 2 + 1) * (radius * 2 + 1);
				else
					value = -1;
				break;
			case laplacian:
				// 拉普拉斯边缘检测
				if (v.x == 0 && v.y == 0)
					value = (radius * 2 + 1) * (radius * 2 + 1) - 1;
				else
					value = -1;
				break;
			default:
				break;
		}
		return value;
	}

	private void setUniform() {
		filterShader.setUniformi("filterName", type.name);
		switch (type) {
			case gaussian:
			case median:
			case laplacian:
			case sharpen:
				if (updateWeights)
					calculateWeights();
				filterShader.setUniform1fv("weights", weights, 0, weights.length);
			case dilate:
			case erode:
				filterShader.setUniformi("radius", radius);
				filterShader.setUniformf("size", new Vector2(getPrefWidth(), getPrefHeight()));
				break;
			case gray:
				filterShader.setUniformf("sepiaTone", new Vector3(1, 1, 1));
				break;
			case sepia:
				filterShader.setUniformf("sepiaTone", sepiaTone);
				break;
			default:
				break;
		}
	}

	public enum FilterType {
		/** 1.无滤镜 */
		none(0),
		/** 2.高斯模糊 */
		gaussian(1),
		/** 3.均值模糊 */
		median(1),
		/** 4.锐化 */
		sharpen(1),
		/** 5.拉普拉斯边缘检测 */
		laplacian(1),
		/** 6.膨胀 */
		dilate(2),
		/** 7.腐蚀 */
		erode(3),
		/** 8.灰度 */
		gray(4),
		/** 9.泛黄，即黑白照，与灰度类型类似，但是可调整响应数值 */
		sepia(4),
		/** 10.反色 */
		negative(5);

		public int name;

		FilterType(int name) {
			this.name = name;
		}
	}
}