package com.goog.video.filter.blur

import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.model.FloatDelegate
import com.goog.video.model.IntDelegate
import com.goog.video.utils.checkArgs
import kotlin.math.max

///优化后的高斯模糊
///具有更好的模糊质量和可控的参数
class GLGaussianBlur2Filter : GLFilter() {

    private var blurRadius by IntDelegate(15,1,30)

    var sampleFactor by FloatDelegate(8f, 1f)

    ///允许采样率动态变化
    private var maxSize: Int? = null

    fun setBlurRadius(size: Int) {
        checkArgs(size > 0)
        if (size % 2 == 0) {
            this.blurRadius = size + 1
        } else {
            this.blurRadius = size
        }
    }

    fun setMaxSize(maxSize: Int?) {
        checkArgs(maxSize == null || maxSize > 0)
        this.maxSize = maxSize
    }

    override fun onDraw(fbo: FrameBufferObject?) {
        val w = fbo?.width ?: 0
        val h = fbo?.height ?: 0
        val frameMaxSize = max(w, h).toFloat()
        var scale = sampleFactor
        val ms = maxSize
        if (ms != null && frameMaxSize > ms) {
            val s = frameMaxSize / ms
            if (s > scale) {
                scale = s
            }
        }
        val size: Float = frameMaxSize / scale

        put("sTextureSize", size)
        put("sSamples", blurRadius)
    }

    override fun getFragmentShader(): String {
        return """
           varying highp vec2 vTextureCoord;

           uniform lowp sampler2D sTexture;
           uniform int sSamples;
           uniform float sTextureSize;

           void main() {
               float mSigmaX = 5.0;
               float mSigmaY = mSigmaX;
               int halfSamples = sSamples / 2;

               float mSigmaX2 = 2.0 * mSigmaX * mSigmaX;
               float mSigmaY2 = 2.0 * mSigmaY * mSigmaY;

               float mPixelSize = 1.0 / sTextureSize;

               float total = 0.0;
               vec3 ret = vec3(0.0);

               for (int iy = 0; iy < sSamples; ++iy) {
                   float offsetY = float(iy - halfSamples);
                   float fy = exp(-offsetY * offsetY / mSigmaY2);
                   offsetY = offsetY * mPixelSize;

                   for (int ix = 0; ix < sSamples; ++ix) {
                       float offsetX = float(ix - halfSamples);
                       float fx = exp(-offsetX * offsetX / mSigmaX2);

                       offsetX = offsetX * mPixelSize;
                       total += fx * fy;
                       ret += texture2D(sTexture, vTextureCoord + vec2(offsetX, offsetY)).rgb * fx * fy;
                   }
               }
               gl_FragColor = vec4(ret / total, 1.0);
           }
        """.trimIndent()
    }


}