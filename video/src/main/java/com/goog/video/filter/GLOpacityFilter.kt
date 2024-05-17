package com.goog.video.filter

import com.goog.video.gl.FrameBufferObject
import com.goog.video.utils.checkArgs

/**
 * Adjusts the alpha channel of the incoming image
 * opacity: The value to multiply the incoming alpha channel for each pixel by (0.0 - 1.0, with 1.0 as the default)
 */
class GLOpacityFilter(opacity: Float = 1f) : GLFilter() {
    private var opacity: Float = 1f

    init {
        setOpacity(opacity)
    }

    fun setOpacity(v: Float) {
        checkArgs(v in 0f..1f, "opacity must be in [0, 1]")
        this.opacity = v
    }

    override fun onDraw(fbo: FrameBufferObject?) {
        put("opacity", opacity)
    }

    override fun getFragmentShader(): String {
        return """
            precision mediump float;
            varying highp vec2 vTextureCoord;

            uniform lowp sampler2D sTexture;
            uniform lowp float opacity;

            void main() {
                lowp vec4 textureColor = texture2D(sTexture, vTextureCoord);

                gl_FragColor = vec4(textureColor.rgb, textureColor.a * opacity);
            }
        """.trimIndent()
    }
}
