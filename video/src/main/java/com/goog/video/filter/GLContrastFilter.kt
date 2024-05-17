package com.goog.video.filter

import com.goog.video.gl.FrameBufferObject
import com.goog.video.utils.checkArgs


/**
 * Changes the contrast of the image.
 * contrast value ranges from 0.0 to 4.0, with 1.0 as the normal level
 */
class GLContrastFilter(contrast: Float = 1.2F) : GLFilter() {
    private var contrast = 1.2f

    init {
        setContrast(contrast)
    }

    fun setContrast(value: Float) {
        checkArgs(value in 0.0..4.0, "Contrast value should be between 0.0 and 4.0")
        this.contrast = value
    }

    override fun onDraw(fbo: FrameBufferObject?) {
        put("contrast", contrast)
    }

    override fun getFragmentShader(): String {
        return """
            precision mediump float;
            varying vec2 vTextureCoord;

            uniform lowp sampler2D sTexture;
            uniform lowp float contrast;

            void main() {
                lowp vec4 textureColor = texture2D(sTexture, vTextureCoord);
                gl_FragColor = vec4(((textureColor.rgb - vec3(0.5)) * contrast + vec3(0.5)), textureColor.w);
            }
        """.trimIndent()
    }

}
