package com.goog.video.filter

import com.goog.video.gl.FrameBufferObject
import com.goog.video.utils.checkArgs

class GLMonochromeFilter(intensity: Float = 1f) : GLFilter() {
    private var intensity: Float = 1.0f

    init {
        setIntensity(intensity)
    }

    fun setIntensity(v: Float) {
        checkArgs(v in 0f..1f, "intensity must be in [0, 1]")
        this.intensity = v
    }
    override fun onDraw(fbo: FrameBufferObject?) {
        put("intensity", intensity)
    }

    //TODO 其它参数范围待确认
    override fun getFragmentShader(): String {
        return """
            precision lowp float;
            varying highp vec2 vTextureCoord;
            uniform lowp sampler2D sTexture;
            uniform float intensity;
            const mediump vec3 luminanceWeighting = vec3(0.2125, 0.7154, 0.0721);
            void main() {
                lowp vec4 textureColor = texture2D(sTexture, vTextureCoord);
                float luminance = dot(textureColor.rgb, luminanceWeighting);
                lowp vec4 desat = vec4(vec3(luminance), 1.0);
                lowp vec4 outputColor = vec4(
                    (desat.r < 0.5 ? (2.0 * desat.r * 0.6) : (1.0 - 2.0 * (1.0 - desat.r) * (1.0 - 0.6))),
                    (desat.g < 0.5 ? (2.0 * desat.g * 0.45) : (1.0 - 2.0 * (1.0 - desat.g) * (1.0 - 0.45))),
                    (desat.b < 0.5 ? (2.0 * desat.b * 0.3) : (1.0 - 2.0 * (1.0 - desat.b) * (1.0 - 0.3))),
                    1.0);
                gl_FragColor = vec4(mix(textureColor.rgb, outputColor.rgb, intensity), textureColor.a);
            }
        """.trimIndent()
    }
}
