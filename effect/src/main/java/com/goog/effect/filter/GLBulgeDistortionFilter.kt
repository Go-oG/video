package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

class GLBulgeDistortionFilter : GLFilter() {

    var centerX by FloatDelegate(0.5f, 0f, 1f)
    var centerY by FloatDelegate(0.5f, 0f, 1f)
    var radius by FloatDelegate(0.5f, 0f, 1f)
    var scale by FloatDelegate(0.5f, 0f, 1f)

    override fun onDraw(fbo: FrameBufferObject?) {
        putVec2("center", centerX, centerY)
        put("radius", if (mEnable) radius else 0f)
        put("scale", if (mEnable) scale else -1f)
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/bulge_distortion.frag")
    }
}
