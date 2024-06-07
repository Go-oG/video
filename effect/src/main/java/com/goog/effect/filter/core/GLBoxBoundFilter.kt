package com.goog.effect.filter.core

import com.goog.effect.gl.FrameBufferObject

abstract class GLBoxBoundFilter : GLFilter() {

    override fun onDraw(fbo: FrameBufferObject?) {
        put("texelWidth", width.toFloat())
        put("texelHeight", height.toFloat())
    }

    override fun getVertexShader(): String {
       return """
           attribute vec4 aPosition;
           attribute vec4 aTextureCoord;

           uniform highp float texelWidth;
           uniform highp float texelHeight;

           varying vec2 vTextureCoord;

           varying vec2 leftTextureCoord;
           varying vec2 topTextureCoord;
           varying vec2 rightTextureCoord;
           varying vec2 bottomTextureCoord;

           varying vec2 topLeftTextureCoord;
           varying vec2 topRightTextureCoord;
           varying vec2 bottomLeftTextureCoord;
           varying vec2 bottomRightTextureCoord;


           void main() {
               gl_Position = aPosition;

               vec2 widthStep = vec2(texelWidth, 0.0);
               vec2 heightStep = vec2(0.0, texelHeight);
               vec2 widthHeightStep = vec2(texelWidth, texelHeight);
               vec2 widthNegativeHeightStep = vec2(texelWidth, -texelHeight);

               vTextureCoord = aTextureCoord.xy;
               leftTextureCoord = aTextureCoord.xy - widthStep;
               rightTextureCoord = aTextureCoord.xy + widthStep;

               topTextureCoord = aTextureCoord.xy - heightStep;
               topLeftTextureCoord = aTextureCoord.xy - widthHeightStep;
               topRightTextureCoord = aTextureCoord.xy + widthNegativeHeightStep;

               bottomTextureCoord = aTextureCoord.xy + heightStep;
               bottomLeftTextureCoord = aTextureCoord.xy - widthNegativeHeightStep;
               bottomRightTextureCoord = aTextureCoord.xy + widthHeightStep;
           }
       """.trimIndent()
    }

}