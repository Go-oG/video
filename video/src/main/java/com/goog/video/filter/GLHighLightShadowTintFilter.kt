package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.model.FColor
import com.goog.video.model.Float3
import com.goog.video.model.FloatDelegate
import com.goog.video.utils.loadFilterFromAsset

class GLHighLightShadowTintFilter : GLFilter() {
    var shadowTintIntensity by FloatDelegate(1f)
    var highlightTintIntensity by FloatDelegate(1f)

    var shadowTintColor = FColor()
    var highlightTintColor = FColor()

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("shadowTintIntensity", shadowTintIntensity)
        put("highlightTintIntensity", highlightTintIntensity)
        putColor("shadowTintColor", shadowTintColor)
        putColor("highlightTintColor", highlightTintColor)
    }


    override fun getFragmentShader(): String {
        return loadFilterFromAsset("highlightShadowTint.fsh")
    }
}