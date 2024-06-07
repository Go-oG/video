package com.goog.effect.filter.blur

import com.goog.effect.filter.core.GLCenterFilter
import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.model.IntDelegate

//径向模糊
open class GLRadialBlurFilter(val useGaussianKernel: Boolean = false) : GLCenterFilter() {
    var blurAmount by FloatDelegate(0.1f, 0f, 1f)
    var sampleCount by IntDelegate(7, 1)
    ///高斯系数 只在使用高斯内核时有用
    var gaussianFactor by FloatDelegate(2f, 0f)

    override fun onDraw(fbo: FrameBufferObject?) {
        putVec2("uCenter", centerX, centerY)
        put("uBlurAmount", blurAmount)
        put("uLoopCount", sampleCount)
        if (useGaussianKernel) {
            put("uFactor", gaussianFactor)
        }
    }

    override fun getFragmentShader(): String {
        return if (useGaussianKernel) SHADER_GAUSSIAN else SHADER_NORMAL
    }

    companion object {

        private const val SHADER_NORMAL = """
            precision mediump float;
            varying highp vec2 vTextureCoord;
            uniform sampler2D sTexture;

            uniform float uBlurAmount;
            uniform vec2 uCenter;
            uniform int uLoopCount;

            void main() {
                if (uBlurAmount <= 0.0) {
                    gl_FragColor = texture2D(sTexture, vTextureCoord);
                } else {
                    vec4 color = vec4(0.0);
                    float totalWeight = 0.0;
                    for (int i = 0; i < uLoopCount; i++) {
                        float t = float(i) / float(uLoopCount - 1);
                        vec2 offset = (vTextureCoord - uCenter) * t * uBlurAmount;
                        color += texture2D(sTexture, vTextureCoord - offset);
                        totalWeight += 1.0;
                    }
                    gl_FragColor = color / totalWeight;
                }
            }
        """

        private const val SHADER_GAUSSIAN = """
            precision mediump float;
            varying highp vec2 vTextureCoord;
            uniform sampler2D sTexture;
            uniform float uBlurAmount;
            uniform float uFactor;
            uniform vec2 uCenter;
            uniform int uLoopCount;

            void main() {
                vec4 color = vec4(0.0);
                float totalWeight = 0.0;
                float blurStrength = uBlurAmount / float(uLoopCount);
                for (int i = 0; i < uLoopCount; i++) {
                    float t = float(i) / float(uLoopCount - 1);
                    float weight = exp(-t * t * uFactor);
                    vec2 offset = (vTextureCoord - uCenter) * t * uBlurAmount;
                    color += texture2D(sTexture, vTextureCoord - offset) * weight;
                    totalWeight += weight;
                }
                gl_FragColor = color / totalWeight;
            }
        """

    }
}

