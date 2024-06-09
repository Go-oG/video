package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate

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

            uniform  sampler2D sTexture;
            uniform  float opacity;

            void main() {
                 vec4 textureColor = texture2D(sTexture, vTextureCoord);
                gl_FragColor = textureColor * opacity;
            }
        """.trimIndent()
    }
}
