package com.goog.effect.filter.blur

import com.goog.effect.filter.core.GLFilterGroup2
import com.goog.effect.filter.core.GLSampleFilter
import com.goog.effect.filter.core.IterationInfo
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate

/**
 * Dual Kawase Blur 实现
 * 其模糊效果和高斯模糊相接近
 * 但其计算量更小，性能更好
 */
class GLDualKawaseBlurFilter : GLFilterGroup2(
    listOf(DownSampleBlur(), UpSampleBlur())
) {

    init {
        setIteratorCount(2)
    }

    fun setIteratorCount(count: Int) {
        for (filter in mFilters) {
            (filter as GLSampleFilter).setIteratorCount(count)
        }
    }

    fun setBlurSize(size: Float) {
        for (filter in mFilters) {
            (filter as BaseSampleBlur).blurRadius = size
        }
    }

}

private abstract class BaseSampleBlur(sample: Float = 2f, upSample: Boolean = false) : GLSampleFilter(sample, upSample) {
    var blurRadius by FloatDelegate(3f, 0f)

    override fun onDraw2(fbo: FrameBufferObject?, info: IterationInfo) {
        super.onDraw2(fbo, info)
        put("uOffset", if (mEnable) blurRadius else 0f)
        putVec2("uHalfPixel", 0.5f / width, 0.5f / height)
    }
}

private class DownSampleBlur : BaseSampleBlur(2f, false) {

    override fun getFragmentShader(): String {
        return """
            precision highp float;
            varying highp vec2 vTextureCoord;
            uniform sampler2D sTexture;

            uniform float uOffset;
            uniform vec2 uHalfPixel;

            void main() {
                vec4 sum = texture2D(sTexture, vTextureCoord) * 4.0;
                sum += texture2D(sTexture, vTextureCoord - uHalfPixel.xy * uOffset);
                sum += texture2D(sTexture, vTextureCoord + uHalfPixel.xy * uOffset);
                sum += texture2D(sTexture, vTextureCoord + vec2(uHalfPixel.x, -uHalfPixel.y) * uOffset);
                sum += texture2D(sTexture, vTextureCoord - vec2(uHalfPixel.x, -uHalfPixel.y) * uOffset);

                //sum/8.0
                gl_FragColor = sum * 0.125;
            }
        """.trimIndent()
    }
}

private class UpSampleBlur : BaseSampleBlur(1f, true) {

    override fun getFragmentShader(): String {
        return """
           precision highp float;
           uniform sampler2D sTexture;
           varying highp vec2 vTextureCoord;

           uniform float uOffset;
           uniform vec2 uHalfPixel;

           void main() {
               vec4 sum = texture2D(sTexture, vTextureCoord + vec2(-uHalfPixel.x * 2.0, 0.0) * uOffset);
               sum += texture2D(sTexture, vTextureCoord + vec2(-uHalfPixel.x, uHalfPixel.y) * uOffset) * 2.0;
               sum += texture2D(sTexture, vTextureCoord + vec2(0.0, uHalfPixel.y * 2.0) * uOffset);
               sum += texture2D(sTexture, vTextureCoord + vec2(uHalfPixel.x, uHalfPixel.y) * uOffset) * 2.0;
               sum += texture2D(sTexture, vTextureCoord + vec2(uHalfPixel.x * 2.0, 0.0) * uOffset);
               sum += texture2D(sTexture, vTextureCoord + vec2(uHalfPixel.x, -uHalfPixel.y) * uOffset) * 2.0;
               sum += texture2D(sTexture, vTextureCoord + vec2(0.0, -uHalfPixel.y * 2.0) * uOffset);
               sum += texture2D(sTexture, vTextureCoord + vec2(-uHalfPixel.x, -uHalfPixel.y) * uOffset) * 2.0;
               //sum / 12.0
               gl_FragColor = sum * 0.0833;
           }
        """.trimIndent()
    }
}

