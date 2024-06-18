package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FillType
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.dp
import com.goog.effect.utils.loadFilterFromAsset

class GLCornerFilter : GLFilter() {
    var fileType = FillType.TRANSPARENT
    var topLeftRadius by FloatDelegate(8.dp, 0f)
    var topRightRadius by FloatDelegate(18.dp, 0f)
    var bottomLeftRadius by FloatDelegate(16.dp, 0f)
    var bottomRightRadius by FloatDelegate(8.dp, 0f)

    override fun onDraw(fbo: FrameBufferObject?) {
        put("uFillType", if (mEnable) fileType.type else 0)
        putVec4("uCornerRadius", topLeftRadius, topRightRadius, bottomRightRadius, bottomLeftRadius)
        putVec2("uResolution", width.toFloat(), height.toFloat())
    }

    override fun getFragmentShader(): String {
       return loadFilterFromAsset("filters/corner.frag")
    }

}

