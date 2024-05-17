package com.goog.video.filter

import com.goog.video.filter.core.GLFilter

class GLInvertFilter : GLFilter() {
    override fun getFragmentShader(): String {
        return """
            precision mediump float;
            varying vec2 vTextureCoord;
            uniform lowp sampler2D sTexture;
            void main() {
                lowp vec4 color = texture2D(sTexture, vTextureCoord);
                gl_FragColor = vec4((1.0 - color.rgb), color.w);
            }
        """.trimIndent()
    }
}
