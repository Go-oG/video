package com.goog.effect.filter

import com.goog.effect.filter.core.GLBoxBoundFilter
import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.utils.loadFilterFromAsset

class GLLaplacianFilter : GLBoxBoundFilter() {

    override fun getFragmentShader(): String {
        return """
            precision highp float;

            varying vec2 vTextureCoord;
            uniform sampler2D sTexture;

            varying vec2 leftTextureCoord;
            varying vec2 topTextureCoord;
            varying vec2 rightTextureCoord;
            varying vec2 bottomTextureCoord;

            varying vec2 topLeftTextureCoord;
            varying vec2 topRightTextureCoord;
            varying vec2 bottomLeftTextureCoord;
            varying vec2 bottomRightTextureCoord;


            void main() {
                mediump vec3 bottomColor = texture2D(sTexture, bottomTextureCoord).rgb;
                mediump vec3 bottomLeftColor = texture2D(sTexture, bottomLeftTextureCoord).rgb;
                mediump vec3 bottomRightColor = texture2D(sTexture, bottomRightTextureCoord).rgb;
                mediump vec4 centerColor = texture2D(sTexture, vTextureCoord);
                mediump vec3 leftColor = texture2D(sTexture, leftTextureCoord).rgb;
                mediump vec3 rightColor = texture2D(sTexture, rightTextureCoord).rgb;
                mediump vec3 topColor = texture2D(sTexture, topTextureCoord).rgb;
                mediump vec3 topRightColor = texture2D(sTexture, topRightTextureCoord).rgb;
                mediump vec3 topLeftColor = texture2D(sTexture, topLeftTextureCoord).rgb;
                
                mediump vec3 resultColor = topLeftColor * 0.5 + topColor * 1.0 + topRightColor * 0.5;
                resultColor += leftColor * 1.0 + centerColor.rgb * (-6.0) + rightColor * 1.0;
                resultColor += bottomLeftColor * 0.5 + bottomColor * 1.0 + bottomRightColor * 0.5;
                
                // Normalize the results to allow for negative gradients in the 0.0-1.0 colorspace
                resultColor = resultColor + 0.5;
                
                gl_FragColor = vec4(resultColor, centerColor.a);
            }
        """.trimIndent()
    }


}