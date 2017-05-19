#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif
varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform float weights[900];
uniform int radius;
uniform vec2 size;
uniform vec3 sepiaTone;
uniform int filterName;

void main()
{
    vec3 rgb;
    vec4 sample = texture2D(u_texture, v_texCoords);
    if(filterName == 0){
        rgb = texture2D(u_texture, v_texCoords).rgb;
    }

    if(filterName == 1){
        int index = 0;
        for (int i = -radius; i <= radius; i++){
            for (int j = -radius; j <= radius; j++){
                rgb += texture2D(u_texture, v_texCoords + (i, j)/size).rgb * weights[index++];
            }
        }
    }

    if(filterName == 2){
        rgb = vec3(0.0);
        for (int i = -radius; i <= radius; i++){
            for (int j = -radius; j <= radius; j++){
                rgb = max(texture2D(u_texture, v_texCoords + (i, j)/size).rgb, rgb);
            }
        }
    }

    if(filterName == 3){
        rgb = vec3(1.0);
        for (int i = -radius; i <= radius; i++){
            for (int j = -radius; j <= radius; j++){
                rgb = min(texture2D(u_texture, v_texCoords + (i, j)/size).rgb, rgb);
            }
        }
    }

    if(filterName == 4){
        float gray = dot(sample.rgb, vec3(0.299, 0.587, 0.114));
        rgb = gray * sepiaTone;
    }

    if(filterName == 5){
        rgb = vec3(1.0 - sample.rgb);
    }

    gl_FragColor = vec4(rgb, sample.a) * v_color;
}
