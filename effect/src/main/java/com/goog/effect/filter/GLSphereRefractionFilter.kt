package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

class GLSphereRefractionFilter : GLFilter() {

    var centerX by FloatDelegate(0.5f, 0f, 1f)
    var centerY by FloatDelegate(0.5f, 0f, 1f)
    var radius by FloatDelegate(0.5f, 0f, 1f)
    var aspectRatio by FloatDelegate(1f, 0.00001f)
    var refractiveIndex by FloatDelegate(0.71f, 0f, 1f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        putVec2("center", centerX, centerY)
        put("radius", radius)
        put("aspectRatio", aspectRatio)
        put("refractiveIndex", refractiveIndex)
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/sphere_refraction.frag")
    }
}
