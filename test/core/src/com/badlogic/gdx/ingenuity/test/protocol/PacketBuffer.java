package com.badlogic.gdx.ingenuity.test.protocol;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

import com.alibaba.fastjson.JSONObject;

/**
 * @作者 mitkey
 * @时间 2017年6月3日 下午12:24:20
 * @类说明 PacketBuffer.java <br/>
 * @版本 0.0.1
 */
public class PacketBuffer {

	/**
	 * <pre>
	 * 	信息的长度 4 个字节 ---- 协议的版本 1 个字节 ---- 状态码 1 个字节 ---- 消息的 id 标识 4 个字节 --- 接口名字的长度 2 个字节 ---- 接口名字 bytes ... ---- 消息体 bytes ...
	 * 	int					byte					byte			int 					short					byte[]					byte[]
	 * </pre>
	 **/

	// 固定消息需要的字节长度
	private static final int FIXED_MSG_LEN = 1 + 1 + 4 + 2;

	private static final AtomicInteger RI = new AtomicInteger(0);

	private PacketBuffer() {
	}

	/** 编码 */
	public static byte[] encode(String si, JSONObject body) {
		byte[] bytesSi = si.getBytes();
		byte[] bytesBody = body.toJSONString().getBytes();

		int siLen = bytesSi.length;
		int bodyLen = bytesBody.length;
		// 消息的总长度
		int msgLen = FIXED_MSG_LEN + siLen + bodyLen;

		ByteBuffer byteBuffer = ByteBuffer.allocate(msgLen + 4);// 加 4 是因为标识消息长度的需要 4 个字节
		// 消息的长度 4 个字节，int
		byteBuffer.putInt(msgLen);
		// 协议的版本 1 个字节，byte
		byteBuffer.put((byte) 1);
		// 状态码 1 个字节，byte
		byteBuffer.put((byte) 0);
		// 消息 id 标识 4 个字节，int
		byteBuffer.putInt(RI.incrementAndGet());
		// 接口名字占用的字节数 2 个字节，short
		byteBuffer.putShort((short) siLen);
		// 接口名字 byte[]
		byteBuffer.put(bytesSi);
		// 消息体 byte[]
		byteBuffer.put(bytesBody);

		byteBuffer.flip();
		byte[] data = new byte[byteBuffer.remaining()];
		byteBuffer.get(data);
		return data;
	}

	/** 解码 */
	public static JeffData decode(byte[] bs) {
		ByteBuffer byteBuffer = ByteBuffer.wrap(bs);

		// 该消息的长度
		int msgLen = byteBuffer.getInt();
		// 协议版本
		byte protocolVersion = byteBuffer.get();
		// 状态码
		byte ex = byteBuffer.get();
		// 消息 id
		int ri = byteBuffer.getInt();

		// 接口名字占用字节数
		short siLen = byteBuffer.getShort();
		// 接口的名字
		byte[] siNameBytes = new byte[siLen];
		byteBuffer.get(siNameBytes);

		// 消息体占用字节数
		int bodyLen = msgLen - FIXED_MSG_LEN - siLen;
		// 消息体
		byte[] bodyBytes = new byte[bodyLen];
		byteBuffer.get(bodyBytes);

		return new JeffData(protocolVersion, ri, ex, new String(siNameBytes), new String(bodyBytes));
	}

	public static final class JeffData {
		private byte protocolVersion;
		private int ri;
		private byte ex;
		private String si;
		private String body;

		public JeffData(byte protocolVersion, int ri, byte ex, String si, String body) {
			super();
			this.protocolVersion = protocolVersion;
			this.ri = ri;
			this.ex = ex;
			this.si = si;
			this.body = body;
		}

		public byte getProtocolVersion() {
			return protocolVersion;
		}

		public int getRi() {
			return ri;
		}

		public byte getEx() {
			return ex;
		}

		public String getSi() {
			return si;
		}

		public String getBody() {
			return body;
		}
	}

}
