package com.goog.video.filter

import com.goog.video.gl.EFrameBufferObject

/**
 * exposure: The adjusted exposure (-10.0 - 10.0, with 0.0 as the default)
 */
class GlExposureFilter : GlFilter() {
    private var exposure = 1f

    fun setExposure(exposure: Float) {
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
