package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.model.FloatDelegate
import com.goog.video.utils.checkArgs

/**
 * Adjusts the alpha channel of the incoming image
 * opacity: The value to multiply the incoming alpha channel for each pixel by (0.0 - 1.0, with 1.0 as the default)
 */
class GLOpacityFilter : GLFilter() {
    var opacity by FloatDelegate(1f, 0f, 1f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
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
