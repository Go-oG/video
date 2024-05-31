package com.goog.video.filter.blur

import com.goog.video.filter.core.GLFilter
import com.goog.video.filter.core.GLFilterGroup
import com.goog.video.gl.FrameBufferObject
import com.goog.video.model.FloatDelegate

//双重模糊
class GLDualBlurFilter : GLFilterGroup() {
    private val verticalBlur = DUalInnerBlur(true)
    private val horizontalBlur = DUalInnerBlur(false)

    init {
        filters = listOf(verticalBlur, horizontalBlur)
    }

    fun setBlurSize(blurSize: Float) {
        verticalBlur.blurSize = blurSize
        horizontalBlur.blurSize = blurSize
    }

    fun setWeightAndOffset(weights: FloatArray, offsets: FloatArray) {
        verticalBlur.setWeightAndOffset(weights, offsets)
        horizontalBlur.setWeightAndOffset(weights, offsets)
    }
}

private class DUalInnerBlur(val vertical: Boolean) : GLFilter() {
    var blurSize by FloatDelegate(1f, 1f)

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
        put("uBlurSize", blurSize)
        put("uSampleCount", sampleCount)
        putArray("uWeights", weights)
        putArray("uOffsets", offsets)
        put("uDir", if (vertical) 1 else 0)
    }

    override fun getFragmentShader(): String {
        return """
            precision mediump float;
            varying vec2 vTextureCoord;
            uniform sampler2D sTexture;
            uniform float uBlurSize;
            uniform int uSampleCount;         // 采样数量
            uniform float uWeights[30];       // 权重数组
            uniform float uOffsets[30];       // 偏移数组
            uniform int uDir;

            void main() {
                vec4 sum = vec4(0.0);
                for (int i = 0; i < uSampleCount; i++) {
                    if(uDir==1){
                     //vertical
                        sum += texture2D(sTexture, vTextureCoord + vec2(0.0, uOffsets[i] * uBlurSize)) * uWeights[i];
                    }else{
                        sum += texture2D(sTexture, vTextureCoord + vec2(uOffsets[i] * uBlurSize, 0.0)) * uWeights[i];
                    }  
                }
                gl_FragColor = sum;
            }
        """.trimIndent()
    }
}

