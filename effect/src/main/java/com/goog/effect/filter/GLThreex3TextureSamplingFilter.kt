package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject

open class GLThreex3TextureSamplingFilter : GLFilter() {

    override fun onDraw(fbo: FrameBufferObject?) {
        putTextureSize()
    }

    override fun getVertexShader(): String {
        return """
            attribute vec4 aPosition;
            attribute vec4 aTextureCoord;
            uniform highp float texelWidth;
            uniform highp float texelHeight;
            varying highp vec2 textureCoordinate;
            varying highp vec2 leftTextureCoordinate;
            varying highp vec2 rightTextureCoordinate;
            varying highp vec2 topTextureCoordinate;
            varying highp vec2 topLeftTextureCoordinate;
            varying highp vec2 topRightTextureCoordinate;
            varying highp vec2 bottomTextureCoordinate;
            varying highp vec2 bottomLeftTextureCoordinate;
            varying highp vec2 bottomRightTextureCoordinate;
            void main() {
                gl_Position = aPosition;
                vec2 widthStep = vec2(texelWidth, 0.0);
                vec2 heightStep = vec2(0.0, texelHeight);
                vec2 widthHeightStep = vec2(texelWidth, texelHeight);
                vec2 widthNegativeHeightStep = vec2(texelWidth, -texelHeight);
                textureCoordinate = aTextureCoord.xy;
                leftTextureCoordinate = textureCoordinate - widthStep;
                rightTextureCoordinate = textureCoordinate + widthStep;
                topTextureCoordinate = textureCoordinate - heightStep;
                topLeftTextureCoordinate = textureCoordinate - widthHeightStep;
                topRightTextureCoordinate = textureCoordinate + widthNegativeHeightStep;
                bottomTextureCoordinate = textureCoordinate + heightStep;
                bottomLeftTextureCoordinate = textureCoordinate - widthNegativeHeightStep;
                bottomRightTextureCoordinate = textureCoordinate + widthHeightStep;
            }
        """.trimIndent()
    }

    override fun getFragmentShader(): String {
        return ""
    }
}