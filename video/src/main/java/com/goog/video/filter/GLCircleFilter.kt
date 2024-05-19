package com.goog.video.filter

import com.goog.video.filter.core.GLCenterFilter
import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.model.FColor4
import com.goog.video.model.FloatDelegate
import com.goog.video.utils.loadFilterFromAsset

class GLCircleFilter : GLCenterFilter() {
    var circleColor = FColor4()
    var backgroundColor = FColor4()
    var radius by FloatDelegate(0.5f)
    var aspectRatio by FloatDelegate(1f, 0f);

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("radius", radius)
        put("aspectRatio", aspectRatio)
        putColor("circleColor", circleColor)
        putColor("backgroundColor", backgroundColor)

    }

    override fun getVertexShader(): String {
        return loadFilterFromAsset("circle.vsh")
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("circle.fsh")
    }
}