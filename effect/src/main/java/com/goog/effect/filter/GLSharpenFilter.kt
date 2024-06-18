package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate

class GLSharpenFilter : GLFilter() {

    var imageWidthFactor by FloatDelegate(0.004f, 0.000001f, 1f)
    var imageHeightFactor by FloatDelegate(0.004f, 0.00001f, 1f)

    var sharpness by FloatDelegate(0f, -4f, 4f)

    override fun onDraw(fbo: FrameBufferObject?) {
        put("imageWidthFactor", imageWidthFactor)
        put("imageHeightFactor", imageHeightFactor)
        put("sharpness", sharpness)
    }

    override fun getVertexShader(): String {
        return """
            attribute vec4 aPosition;
            attribute vec4 aTextureCoord;
            uniform float imageWidthFactor;
            uniform float imageHeightFactor;
            uniform float sharpness;
            varying highp vec2 textureCoordinate;
            varying highp vec2 leftTextureCoordinate;
            varying highp vec2 rightTextureCoordinate;
            varying highp vec2 topTextureCoordinate;
            varying highp vec2 bottomTextureCoordinate;
            varying float centerMultiplier;
            varying float edgeMultiplier;
            void main() {
                gl_Position = aPosition;
                mediump vec2 widthStep = vec2(imageWidthFactor, 0.0);
                mediump vec2 heightStep = vec2(0.0, imageHeightFactor);
                textureCoordinate = aTextureCoord.xy;
                leftTextureCoordinate = textureCoordinate - widthStep;
                rightTextureCoordinate = textureCoordinate + widthStep;
                topTextureCoordinate = textureCoordinate + heightStep;
                bottomTextureCoordinate = textureCoordinate - heightStep;
                centerMultiplier = 1.0 + 4.0 * sharpness;
                edgeMultiplier = sharpness;
            }
        """.trimIndent()
    }

    override fun getFragmentShader(): String {
        return """
            precision mediump float;
            uniform lowp sampler2D sTexture;
            varying vec2 textureCoordinate;
            varying vec2 leftTextureCoordinate;
            varying vec2 rightTextureCoordinate;
            varying vec2 topTextureCoordinate;
            varying vec2 bottomTextureCoordinate;
            varying float centerMultiplier;
            varying float edgeMultiplier;

            void main() {
                vec3 textureColor = texture2D(sTexture, textureCoordinate).rgb;
                vec3 leftTextureColor = texture2D(sTexture, leftTextureCoordinate).rgb;
                vec3 rightTextureColor = texture2D(sTexture, rightTextureCoordinate).rgb;
                vec3 topTextureColor = texture2D(sTexture, topTextureCoordinate).rgb;
                vec3 bottomTextureColor = texture2D(sTexture, bottomTextureCoordinate).rgb;
                gl_FragColor = vec4((textureColor * centerMultiplier - (leftTextureColor * edgeMultiplier + rightTextureColor * edgeMultiplier + topTextureColor * edgeMultiplier + bottomTextureColor * edgeMultiplier)), texture2D(sTexture, bottomTextureCoordinate).w);
            }
        """.trimIndent()
    }
}
