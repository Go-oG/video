package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate

class GLBilateralFilter : GLFilter() {
    var texelWidthOffset by FloatDelegate(0.003f, 0f)
    var texelHeightOffset by FloatDelegate(0.003f, 0f)
    var blurSize by FloatDelegate(1f, 1f)

    override fun onDraw(fbo: FrameBufferObject?) {
        put("texelWidthOffset", texelWidthOffset)
        put("texelHeightOffset", texelHeightOffset)
        put("blurSize", blurSize)
    }

    public override fun getVertexShader(): String {
        return """
        attribute vec4 aPosition; 
                attribute vec4 aTextureCoord;
                const  int GAUSSIAN_SAMPLES = 9;
                uniform highp float texelWidthOffset; 
                uniform highp float texelHeightOffset;
                uniform highp float blurSize;
                varying highp vec2 vTextureCoord;
                varying highp vec2 blurCoordinates[GAUSSIAN_SAMPLES];
                void main() { 
                    gl_Position = aPosition;
                    vTextureCoord = aTextureCoord.xy; 
                    int multiplier = 0;
                     vec2 blurStep;
                     vec2 singleStepOffset = vec2(texelHeightOffset, texelWidthOffset) * blurSize;
                    for ( int i = 0; i < GAUSSIAN_SAMPLES; i++) {
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
            const  int GAUSSIAN_SAMPLES = 9;
            varying highp vec2 vTextureCoord;
            varying highp vec2 blurCoordinates[GAUSSIAN_SAMPLES];
            const float distanceNormalizationFactor = 1.5;
            void main() {
                vec4 centralColor = texture2D(sTexture, blurCoordinates[4]);
                float gaussianWeightTotal = 0.18;
                vec4 sum = centralColor * 0.18;
                vec4 sampleColor = texture2D(sTexture, blurCoordinates[0]);
                float distanceFromCentralColor;
                distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);
                float gaussianWeight = 0.05 * (1.0 - distanceFromCentralColor);
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
