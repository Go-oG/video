package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.utils.loadFilterFromAsset

//TODO 将像素大小转换为比例大小
class GLAverageColorFilter : GLFilter() {

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("texelWidth", 1f / width)
        put("texelHeight", 1f / height)
    }

    override fun getVertexShader(): String {
      return loadFilterFromAsset("filters/average_color.vert")
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/average_color.frag")
    }
}