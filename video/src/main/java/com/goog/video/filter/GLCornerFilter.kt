package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.model.FillType
import com.goog.video.model.FloatDelegate
import com.goog.video.utils.checkArgs
import com.goog.video.utils.dp
import com.goog.video.utils.loadFilterFromAsset

class GLCornerFilter : GLFilter() {
    var fileType = FillType.TRANSPARENT
    var topLeftRadius by FloatDelegate(8.dp, 0f, maxV = 24.dp)
    var topRightRadius by FloatDelegate(18.dp, 0f, maxV = 24.dp)
    var bottomLeftRadius by FloatDelegate(16.dp, 0f, maxV = 24.dp)
    var bottomRightRadius by FloatDelegate(8.dp, 0f, maxV = 24.dp)

    override fun onDraw(fbo: FrameBufferObject?) {
        put("uFillType",fileType.type)
        putVec4("uCornerRadius", topLeftRadius, topRightRadius, bottomRightRadius, bottomLeftRadius)
        putVec2("uResolution", width.toFloat(), height.toFloat())
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("corner.fsh")
    }

}

