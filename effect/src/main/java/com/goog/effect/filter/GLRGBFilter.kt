package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

class GLRGBFilter : GLFilter() {

    var red by FloatDelegate(1f, 0f, 1f)
    var green by FloatDelegate(1f, 0f, 1f)
    var blue by FloatDelegate(1f, 0f, 1f)
    var brightness by FloatDelegate(1f, 0f, 1f)
    var saturation by FloatDelegate(1f, 0f, 1f)
    var contrast by FloatDelegate(1f, 0f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("red", red)
        put("green", green)
        put("blue", blue)
        put("brightness", brightness)
        put("saturation", saturation)
        put("contrast", contrast)
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/rgb.frag")
    }
}
