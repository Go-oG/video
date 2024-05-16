package com.goog.video.filter

import com.goog.video.gl.EFrameBufferObject
import com.goog.video.utils.checkArgs

class GLBilateralFilter(wOffset: Float = 0.003f, hOffset: Float = 0.003f, blurSize: Float = 1f) : GLFilter() {
    private var texelWidthOffset: Float = 0.003f
    private var texelHeightOffset: Float = 0.003f
    private var blurSize: Float = 1.0f

    init {
        setBlurSize(blurSize)
        setTexelWidthOffset(wOffset)
        setTexelHeightOffset(hOffset)
    }

    fun setTexelWidthOffset(v: Float) {
        checkArgs(v >= 0.0f, "texelWidthOffset must be >= 0")
        texelWidthOffset = v
    }

    fun setTexelHeightOffset(v: Float) {
        checkArgs(v >= 0.0f, "texelHeightOffset must be >= 0")
        texelHeightOffset = v
    }

    fun setBlurSize(v: Float) {
        checkArgs(v >= 1f, "blurSize must be >= 0")
        blurSize = v
    }

    override fun onDraw(fbo: EFrameBufferObject?) {
        put("texelWidthOffset", texelWidthOffset)
        put("texelHeightOffset", texelHeightOffset)
        put("blurSize", blurSize)
    }

    public override fun getVertexShader(): String {
        return """
        attribute vec4 aPosition; 
                attribute vec4 aTextureCoord;
                const lowp int GAUSSIAN_SAMPLES = 9;
                uniform highp float texelWidthOffset; 
                uniform highp float texelHeightOffset;
                uniform highp float blurSize;
                varying highp vec2 vTextureCoord;
                varying highp vec2 blurCoordinates[GAUSSIAN_SAMPLES];
                void main() { 
                    gl_Position = aPosition;
                    vTextureCoord = aTextureCoord.xy; 
                    int multiplier = 0;
                    highp vec2 blurStep;
                    highp vec2 singleStepOffset = vec2(texelHeightOffset, texelWidthOffset) * blurSize;
                    for (lowp int i = 0; i < GAUSSIAN_SAMPLES; i++) {
                        multiplier = (i - ((GAUSSIAN_SAMPLES - 1) / 2)); 
                        blurStep = float(multiplier) * singleStepOffset;
                        blurCoordinates[i] = vTextureCoord.xy + blurStep;
                    }
                }
            """
    }

    public override fun getFragmentShader(): String {
        return """
            precision mediump float;
            uniform lowp sampler2D sTexture;
            const lowp int GAUSSIAN_SAMPLES = 9;
            varying highp vec2 vTextureCoord;
            varying highp vec2 blurCoordinates[GAUSSIAN_SAMPLES];
            const mediump float distanceNormalizationFactor = 1.5;
            void main() {
                lowp vec4 centralColor = texture2D(sTexture, blurCoordinates[4]);
                lowp float gaussianWeightTotal = 0.18;
                lowp vec4 sum = centralColor * 0.18;
                lowp vec4 sampleColor = texture2D(sTexture, blurCoordinates[0]);
                lowp float distanceFromCentralColor;
                distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);
                lowp float gaussianWeight = 0.05 * (1.0 - distanceFromCentralColor);
                gaussianWeightTotal += gaussianWeight;
                sum += sampleColor * gaussianWeight;
                sampleColor = texture2D(sTexture, blurCoordinates[1]);
                distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);
                gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor);
                gaussianWeightTotal += gaussianWeight;
                sum += sampleColor * gaussianWeight;
                sampleColor = texture2D(sTexture, blurCoordinates[2]);
                distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);
                gaussianWeight = 0.12 * (1.0 - distanceFromCentralColor);
                gaussianWeightTotal += gaussianWeight;
                sum += sampleColor * gaussianWeight;
                sampleColor = texture2D(sTexture, blurCoordinates[3]);
                distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);
                gaussianWeight = 0.15 * (1.0 - distanceFromCentralColor);
                gaussianWeightTotal += gaussianWeight;
                sum += sampleColor * gaussianWeight;
                sampleColor = texture2D(sTexture, blurCoordinates[5]);
                distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);
                gaussianWeight = 0.15 * (1.0 - distanceFromCentralColor);
                gaussianWeightTotal += gaussianWeight;
                sum += sampleColor * gaussianWeight;
                sampleColor = texture2D(sTexture, blurCoordinates[6]);
                distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);
                gaussianWeight = 0.12 * (1.0 - distanceFromCentralColor);
                gaussianWeightTotal += gaussianWeight;
                sum += sampleColor * gaussianWeight;
                sampleColor = texture2D(sTexture, blurCoordinates[7]);
                distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);
                gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor);
                gaussianWeightTotal += gaussianWeight;
                sum += sampleColor * gaussianWeight;
                sampleColor = texture2D(sTexture, blurCoordinates[8]);
                distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);
                gaussianWeight = 0.05 * (1.0 - distanceFromCentralColor);
                gaussianWeightTotal += gaussianWeight;
                sum += sampleColor * gaussianWeight;
                gl_FragColor = sum / gaussianWeightTotal;
            }
        """
    }

}
