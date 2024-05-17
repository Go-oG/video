package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.utils.checkArgs

class GLSharpenFilter(sharpness: Float = 0f) : GLFilter() {
    private var imageWidthFactor = 0.004f
    private var imageHeightFactor = 0.004f
    private var sharpness: Float = 1f

    init {
        setsSharpness(sharpness)
    }

    fun setsSharpness(v: Float) {
        checkArgs(v >= -4 && v <= 4, "sharpness must in [-4,4]")
        this.sharpness = v
    }

    override fun setFrameSize(width: Int, height: Int) {
        imageWidthFactor = 1f / width
        imageHeightFactor = 1f / height
    }


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
            precision highp float;
            uniform lowp sampler2D sTexture;
            varying highp vec2 textureCoordinate;
            varying highp vec2 leftTextureCoordinate;
            varying highp vec2 rightTextureCoordinate;
            varying highp vec2 topTextureCoordinate;
            varying highp vec2 bottomTextureCoordinate;
            varying float centerMultiplier;
            varying float edgeMultiplier;

            void main() {
                mediump vec3 textureColor = texture2D(sTexture, textureCoordinate).rgb;
                mediump vec3 leftTextureColor = texture2D(sTexture, leftTextureCoordinate).rgb;
                mediump vec3 rightTextureColor = texture2D(sTexture, rightTextureCoordinate).rgb;
                mediump vec3 topTextureColor = texture2D(sTexture, topTextureCoordinate).rgb;
                mediump vec3 bottomTextureColor = texture2D(sTexture, bottomTextureCoordinate).rgb;
                gl_FragColor = vec4((textureColor * centerMultiplier - (leftTextureColor * edgeMultiplier + rightTextureColor * edgeMultiplier + topTextureColor * edgeMultiplier + bottomTextureColor * edgeMultiplier)), texture2D(sTexture, bottomTextureCoordinate).w);
            }
        """.trimIndent()
    }
}
