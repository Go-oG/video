package com.goog.video.filter

import com.goog.video.filter.core.GLCenterFilter
import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.model.FloatDelegate
import com.goog.video.utils.loadFilterFromAsset

class GLGlassSphereFilter:GLCenterFilter() {
   var radius by FloatDelegate(0.5f,0f,1f)
   var aspectRatio by FloatDelegate(1f,0f)
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