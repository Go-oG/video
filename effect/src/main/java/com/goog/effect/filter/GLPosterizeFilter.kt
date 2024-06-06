package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate

class GLPosterizeFilter : GLFilter() {
    var colorLevels by FloatDelegate(10f, 0f)

    override fun onDraw(fbo: FrameBufferObject?) {
        put("colorLevels", colorLevels)
    }

    override fun getFragmentShader(): String {
        return """
            precision mediump float;
            varying vec2 vTextureCoord;

            uniform lowp sampler2D sTexture;
            uniform highp float colorLevels;

            void main() {
                highp vec4 textureColor = texture2D(sTexture, vTextureCoord);
                gl_FragColor = floor((textureColor * colorLevels) + vec4(0.5)) / colorLevels;
            }
        """.trimIndent()
    }
}
