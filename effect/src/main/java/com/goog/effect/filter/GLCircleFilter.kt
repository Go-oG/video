package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FColor4
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

class GLCircleFilter : GLFilter() {
    var centerX by FloatDelegate(0.5f, 0f, 1f)
    var centerY by FloatDelegate(0.5f, 0f, 1f)

    var circleColor = FColor4()
    var backgroundColor = FColor4()
    var radius by FloatDelegate(0.5f)
    var aspectRatio by FloatDelegate(1f, 0f);

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("uAspectRatio", aspectRatio)
        putVec2("uCenter", centerX, centerY)
        put("uRadius", radius)
        putColor("uCircleColor", circleColor)
        putColor("uBackgroundColor", backgroundColor)
    }

    override fun getVertexShader(): String {
        return loadFilterFromAsset("filters/circle.vert")
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/circle.frag")
    }
}