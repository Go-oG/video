package com.goog.effect.filter

import com.goog.effect.filter.core.GLCenterFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

class GLGlassSphereFilter:GLCenterFilter() {
   var radius by FloatDelegate(0.5f,0f,1f)
   var aspectRatio by FloatDelegate(1f,0f, includeMin = false)
   var refractiveIndex by FloatDelegate(0.2f,0f,1f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("radius",radius)
        put("aspectRatio",aspectRatio)
        put("refractiveIndex",refractiveIndex)
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("glassSphere.fsh")
    }
}