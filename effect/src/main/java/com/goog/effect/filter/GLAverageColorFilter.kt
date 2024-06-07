package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.utils.loadFilterFromAsset

class GLAverageColorFilter :GLFilter() {

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        putTextureSize()
    }
    override fun getVertexShader(): String {
        return """
            attribute vec4 aPosition;
            attribute vec4 aTextureCoord;
            varying highp vec2 vTextureCoord;
            uniform float texelWidth;
            uniform float texelHeight;

            varying vec2 upperLeftInputTextureCoord;
            varying vec2 upperRightInputTextureCoord;
            varying vec2 lowerLeftInputTextureCoord;
            varying vec2 lowerRightInputTextureCoord;

            void main() {
                gl_Position = aPosition;
                vTextureCoord = aTextureCoord.xy;
                upperLeftInputTextureCoord = aTextureCoord.xy + vec2(-texelWidth, -texelHeight);
                upperRightInputTextureCoord = aTextureCoord.xy + vec2(texelWidth, -texelHeight);
                lowerLeftInputTextureCoord = aTextureCoord.xy + vec2(-texelWidth, texelHeight);
                lowerRightInputTextureCoord = aTextureCoord.xy + vec2(texelWidth, texelHeight);
            }
        """.trimIndent()
    }
    override fun getFragmentShader(): String {
        return """
            precision highp float;

            uniform sampler2D sTexture;
            varying highp vec2 vTextureCoord;

            varying highp vec2 upperLeftInputTextureCoord;
            varying highp vec2 upperRightInputTextureCoord;
            varying highp vec2 lowerLeftInputTextureCoord;
            varying highp vec2 lowerRightInputTextureCoord;

            void main() {
                highp vec4 upperLeftColor = texture2D(sTexture, upperLeftInputTextureCoord);
                highp vec4 upperRightColor = texture2D(sTexture, upperRightInputTextureCoord);
                highp vec4 lowerLeftColor = texture2D(sTexture, lowerLeftInputTextureCoord);
                highp vec4 lowerRightColor = texture2D(sTexture, lowerRightInputTextureCoord);

                gl_FragColor = 0.25 * (upperLeftColor + upperRightColor + lowerLeftColor + lowerRightColor);
            }
        """.trimIndent()
    }
}