package com.goog.effect.filter.blur

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate

// 粒状模糊
class GLGrainyBlurFilter : GLFilter() {
    var directionX by FloatDelegate(0f)
    var directionY by FloatDelegate(0f)
    var blurSize by FloatDelegate(1f, 1f)

    var noiseIntensity by FloatDelegate(0f, 0f, 1f)

    ///不能出现负数
    private var sampleCount = 0
    private var weights = FloatArray(30)
    private var offsets = FloatArray(30)

    init {
        val weights = floatArrayOf(0.05f, 0.09f, 0.12f, 0.15f, 0.16f, 0.15f, 0.12f, 0.09f, 0.05f)
        val offsets = floatArrayOf(-4.0f, -3.0f, -2.0f, -1.0f, 0.0f, 1.0f, 2.0f, 3.0f, 4.0f)
        setWeightAndOffset(weights, offsets)
    }

    fun setWeightAndOffset(weights: FloatArray, offsets: FloatArray) {
        check(weights.size == offsets.size && weights.size <= 30)
        sampleCount = weights.size
        System.arraycopy(weights, 0, this.weights, 0, weights.size)
        System.arraycopy(offsets, 0, this.offsets, 0, offsets.size)
    }

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        putVec2("uDirection", directionX, directionY)
        put("uBlurSize", blurSize)
        put("uSampleCount", sampleCount)
        put("uNoiseIntensity", noiseIntensity)
        putArray("uWeights", weights)
        putArray("uOffsets", offsets)
    }

    override fun getFragmentShader(): String {
        return """
            precision mediump float;

            varying vec2 vTextureCoord;
            uniform sampler2D sTexture;
            uniform vec2 uDirection;          // 模糊的方向，x和y分量表示方向矢量
            uniform float uBlurSize;          // 模糊的大小
            uniform int uSampleCount;         // 采样数量
            uniform float uWeights[30];       // 权重数组
            uniform float uOffsets[30];       // 偏移数组
            uniform float uNoiseIntensity;    // 噪声强度

            //简单的随机函数
            float random(vec2 co) {
                return fract(sin(dot(co.xy, vec2(12.9898, 78.233))) * 43758.5453);
            }
            void main() {
                vec4 sum = vec4(0.0);
                vec2 texOffset = uDirection * uBlurSize;
                for (int i = 0; i < uSampleCount; i++) {
                    // 添加噪声到偏移
                    float noise = (random(vTextureCoord + uOffsets[i] * texOffset) - 0.5) * uNoiseIntensity;
                    vec2 noisyOffset = texOffset * (uOffsets[i] + noise);
                    sum += texture2D(sTexture, vTextureCoord + noisyOffset) * uWeights[i];
                }
                gl_FragColor = sum;
            }
        """.trimIndent()
    }
}