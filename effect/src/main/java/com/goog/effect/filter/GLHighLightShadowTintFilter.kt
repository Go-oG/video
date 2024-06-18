package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FColor
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

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
        return loadFilterFromAsset("filters/highlight_shadow_tint.fsh")
    }
}