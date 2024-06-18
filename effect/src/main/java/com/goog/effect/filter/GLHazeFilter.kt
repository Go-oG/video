package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

class GLHazeFilter : GLFilter() {

    //suggest -0.3 ->0.3
    var distance by FloatDelegate(0.2f,-1f,1f)
    //suggest -0.3 ->0.3
    var slope by FloatDelegate(0f,-1f,1f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("distance",distance)
        put("slope",slope)
    }

    override fun getFragmentShader(): String {
      return loadFilterFromAsset("filters/haze.frag")
    }
}
