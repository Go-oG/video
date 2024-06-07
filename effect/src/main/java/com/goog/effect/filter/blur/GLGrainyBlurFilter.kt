package com.goog.effect.filter.blur
import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject

// 粒状模糊
class GLGrainyBlurFilter : GLFilter() {

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        //  put("uBlurSize", 1f)
        put("uScale", 50f)
        //  put("uSampleCount", iteratorCount)
        putVec2("uResolution", width * 1f, height * 1f)
    }

    override fun getFragmentShader(): String {
        return """
                precision mediump float;

                varying vec2 vTextureCoord;
                uniform sampler2D sTexture;
                uniform float uScale;
                uniform vec2 uResolution;

                float rand(vec2 uv) {
                    return fract(sin(dot(uv, vec2(1225.6548, 321.8942))) * 4251.4865);
                }

                void main() {
                    vec2 ps = vec2(1.0) / uResolution.xy;
                    vec2 offset = (rand(vTextureCoord) - 0.5) * 2.0 * ps * uScale;
                    vec3 noise = texture2D(sTexture,  vTextureCoord+ offset).rgb;
                    gl_FragColor = vec4(noise, 1.0);
                }

        """.trimIndent()
    }

}