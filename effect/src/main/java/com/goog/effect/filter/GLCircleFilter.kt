package com.goog.effect.filter

import com.goog.effect.filter.core.GLCenterFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FColor4
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

//TODO 带查看效果
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
        return loadFilterFromAsset("filters/circle.vert")
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/circle.frag")
    }
}