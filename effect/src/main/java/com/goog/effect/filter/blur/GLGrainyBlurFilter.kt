package com.goog.effect.filter.blur

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.model.IntDelegate
import kotlin.math.sin

// 粒状模糊
class GLGrainyBlurFilter : GLFilter() {

    var blurRadius by IntDelegate(15, 1)
    var iteratorCount by IntDelegate(20, 1)
    var sigma by FloatDelegate(5f, 0.0f)
    var noiseRange by FloatDelegate(5f, 0.0001f)


    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("uBlurRadius", blurRadius.toFloat())
        put("uSigma", sigma)
        put("uNoiseRange", noiseRange)
        put("uSampleCount", iteratorCount)
       // putVec2("uTextureSize", width.toFloat(), height.toFloat())

    }

    override fun getFragmentShader(): String {
        return """
            precision mediump float;

            varying vec2 vTextureCoord;
            uniform sampler2D sTexture;

            uniform float uBlurRadius;
            uniform float uSigma;
            uniform float uNoiseRange;
            uniform int uSampleCount;
          //  uniform vec2 uTextureSize;
            
            const float PI = 3.141592653589;

            float rand(vec2 co) {
                return fract(sin(dot(co.xy, vec2(12.9898, 78.233))) * 43758.5453);
            }

            float gaussian(float x, float sigma) {
                return exp(-x * x / (2.0 * sigma * sigma)) / (sigma * sqrt(2.0 * PI));
            }

            void main() {
                vec2 texelSize = vec2(0.5,0.5);
                vec4 color = vec4(0.0);
                float totalWeight = 0.0;
                for (int i = 0; i < uSampleCount; i++) {
                    float offset = float(i) / float(uSampleCount - 1) * uBlurRadius;
                    float noise = rand(vTextureCoord) * uNoiseRange;
                    float vv = gaussian(offset, uSigma);
                    color += texture2D(sTexture, vTextureCoord + vec2(offset * texelSize.x + noise, 0.0)) * vv;
                    totalWeight += vv;
                }
                color /= totalWeight;
                gl_FragColor = color;
            }
        """.trimIndent()
    }
}