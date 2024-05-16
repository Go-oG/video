package com.goog.video.filter

import com.goog.video.gl.EFrameBufferObject
import com.goog.video.utils.checkArgs


class GLExposureFilter(exposure: Float = 1f) : GLFilter() {
    private var exposure = 1f

    init {
        setExposure(exposure)
    }
    fun setExposure(exposure: Float) {
        checkArgs(exposure >= -10f && exposure <= 10f, "exposure must in [-10,10]")
        this.exposure = exposure
    }

    override fun onDraw(fbo: EFrameBufferObject?) {
        put("exposure", exposure)
    }

    override fun getFragmentShader(): String {
        return """
            precision mediump float;
            varying vec2 vTextureCoord;

            uniform lowp sampler2D sTexture;
            uniform highp float exposure;

            void main() {
                highp vec4 textureColor = texture2D(sTexture, vTextureCoord);
                gl_FragColor = vec4(textureColor.rgb * pow(2.0, exposure), textureColor.w);
            } 
        """.trimIndent()
    }

}
