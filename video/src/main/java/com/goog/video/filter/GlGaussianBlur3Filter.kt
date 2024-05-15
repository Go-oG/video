package com.goog.video.filter

import com.goog.video.gl.EFrameBufferObject
import com.goog.video.utils.checkArgs

class GlGaussianBlur3Filter() : GlFilterGroup(listOf()) {
    private var blurSize: Int = 3

    init {
        val list = mutableListOf(
                Blur3Inner(true, blurSize),
                Blur3Inner(false, blurSize))
        filters = list
    }

    fun setBlurSize(size: Int) {
        checkArgs(size > 0)
        this.blurSize = size
        for (filter in filters) {
            if (filter is Blur3Inner) {
                filter.blurSize = size
            }
        }
    }
}

private class Blur3Inner(val horizontalBlur: Boolean, var blurSize: Int = 3) : GlFilter() {

    override fun onDraw(fbo: EFrameBufferObject?) {
        put("useHorizontal", if (horizontalBlur) 1 else 0)
        put("blurSize", blurSize)

        val w = fbo?.width ?: 1
        val h = fbo?.height ?: 1
        put("mTexOffset", 1.0f / w, 1.0f / h)

    }

    override fun getFragmentShader(): String {
        return """
          precision mediump float;

          varying highp vec2 vTextureCoord;
          uniform sampler2D sTexture;
          uniform int blurSize;
          uniform vec2 mTexOffset;
          uniform int useHorizontal;

          void main() {
              vec4 color = vec4(0.0);
              float totalWeight = 0.0;
              int kernelSize = blurSize * 2 + 1;
              float sizePow = 2.0 * blurSize * blurSize;

              for (int i = -blurSize; i <= blurSize; ++i) {
                  float fi = float(i);
                  float weight = exp(-fi * fi / sizePow);
                  vec2 offset = (useHorizontal != 0) ? vec2(fi * mTexOffset.x, 0.0) : vec2(0.0, fi * mTexOffset.y);
                  color += texture(sTexture, vTextureCoord + offset) * weight;
                  totalWeight += weight;
              }
              gl_FragColor = color / totalWeight;
          }
        """.trimIndent()
    }

}