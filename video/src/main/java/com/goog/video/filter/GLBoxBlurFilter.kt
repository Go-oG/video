package com.goog.video.filter

import com.goog.video.gl.EFrameBufferObject
import com.goog.video.utils.checkArgs

class GLBoxBlurFilter : GLFilter() {
    var texelWidthOffset: Float = 0.003f
    var texelHeightOffset: Float = 0.003f
    var blurSize: Float = 1.0f

    override fun onDraw(fbo: EFrameBufferObject?) {
        checkArgs(blurSize>=0)
        checkArgs(texelWidthOffset>=0)
        checkArgs(texelHeightOffset>=0)

        put("texelWidthOffset", texelWidthOffset)
        put("texelHeightOffset", texelHeightOffset)
        put("blurSize", blurSize)
    }

    public override fun getVertexShader(): String {
        return """
            attribute vec4 aPosition;
            attribute vec4 aTextureCoord;
            uniform highp float texelWidthOffset;
            uniform highp float texelHeightOffset;
            uniform highp float blurSize;
            varying highp vec2 centerTextureCoordinate;
            varying highp vec2 oneStepLeftTextureCoordinate;
            varying highp vec2 twoStepsLeftTextureCoordinate;
            varying highp vec2 oneStepRightTextureCoordinate;
            varying highp vec2 twoStepsRightTextureCoordinate;
            void main() {
                gl_Position = aPosition;
                vec2 firstOffset = vec2(1.5 * texelWidthOffset, 1.5 * texelHeightOffset) * blurSize;
                vec2 secondOffset = vec2(3.5 * texelWidthOffset, 3.5 * texelHeightOffset) * blurSize;
                centerTextureCoordinate = aTextureCoord.xy;
                oneStepLeftTextureCoordinate = centerTextureCoordinate - firstOffset;
                twoStepsLeftTextureCoordinate = centerTextureCoordinate - secondOffset;
                oneStepRightTextureCoordinate = centerTextureCoordinate + firstOffset;
                twoStepsRightTextureCoordinate = centerTextureCoordinate + secondOffset;
            }
        """.trimIndent()
    }

    public override fun getFragmentShader(): String {
        return """
            precision mediump float;
            uniform lowp sampler2D sTexture;
            varying highp vec2 centerTextureCoordinate;
            varying highp vec2 oneStepLeftTextureCoordinate;
            varying highp vec2 twoStepsLeftTextureCoordinate;
            varying highp vec2 oneStepRightTextureCoordinate;
            varying highp vec2 twoStepsRightTextureCoordinate;
            void main() {
                lowp vec4 color = texture2D(sTexture, centerTextureCoordinate) * 0.2;
                color += texture2D(sTexture, oneStepLeftTextureCoordinate) * 0.2;
                color += texture2D(sTexture, oneStepRightTextureCoordinate) * 0.2;
                color += texture2D(sTexture, twoStepsLeftTextureCoordinate) * 0.2;
                color += texture2D(sTexture, twoStepsRightTextureCoordinate) * 0.2;
                gl_FragColor = color;
            }
        """.trimIndent()
    }
}
