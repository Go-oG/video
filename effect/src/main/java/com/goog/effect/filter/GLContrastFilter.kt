package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate


/**
 * Changes the contrast of the image.
 * contrast value ranges from 0.0 to 4.0, with 1.0 as the normal level
 */
class GLContrastFilter: GLFilter() {

    var contrast by FloatDelegate(1f, 0f, 4f)

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
