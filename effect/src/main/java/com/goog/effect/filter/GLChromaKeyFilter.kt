package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FColor
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

class GLChromaKeyFilter : GLFilter() {
    var threshold by FloatDelegate(0.01f, 0f, 1f)
    var smoothing by FloatDelegate(0.05f, 0f, 1f)

    var replaceColor = FColor()


    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("uThreshold", threshold)
        put("uSmoothing", smoothing)
        putColor("uReplaceColor", replaceColor)
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/chroma_key.fsh")
    }
}