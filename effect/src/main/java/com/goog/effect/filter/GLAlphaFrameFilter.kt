package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter

class GLAlphaFrameFilter : GLFilter() {

    override fun getVertexShader(): String {
        return """
            attribute vec4 aPosition;
            attribute vec4 aTextureCoord;
            varying highp vec2 vTextureCoord;
            varying highp vec2 vTextureCoord2;
            void main() {
                gl_Position = aPosition;
                vTextureCoord = vec2(aTextureCoord.x, aTextureCoord.y * 0.5 + 0.5);
                vTextureCoord2 = vec2(aTextureCoord.x, aTextureCoord.y * 0.5);
            }
        """.trimIndent()
    }

    override fun getFragmentShader(): String {
        return """
            precision mediump float;
            varying highp vec2 vTextureCoord;
            varying highp vec2 vTextureCoord2;
            uniform lowp sampler2D sTexture;
            void main() {
                vec4 color1 = texture2D(sTexture, vTextureCoord);
                vec4 color2 = texture2D(sTexture, vTextureCoord2);
                gl_FragColor = vec4(color1.rgb, color2.r);
            }
        """.trimIndent()
    }
}