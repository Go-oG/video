package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.utils.loadFilterFromAsset

class GLColorSwizzlingFilter:GLFilter() {
    override fun getFragmentShader(): String {
        return """
            varying highp vec2 vTextureCoord;
            uniform sampler2D sTexture;

            void main() {
                gl_FragColor = texture2D(sTexture, vTextureCoord).bgra;
            }
        """.trimIndent()
    }
}