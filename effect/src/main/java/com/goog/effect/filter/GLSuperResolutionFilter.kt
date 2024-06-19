package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

/**
 * 超分辨率
 * shader copy from
 * https://github.com/SnapdragonStudios/snapdragon-gsr/blob/main/include/glsl/sgsr_shader_mobile.frag
 */
class GLSuperResolutionFilter : GLFilter() {

    var pixelMode = SRPixelMode.RGBA
    var edgeThreshold by FloatDelegate(8.0f / 255.0f, 0f, 1f)
    var edgeSharpness by FloatDelegate(2f, 0f)

    override fun onDraw(fbo: FrameBufferObject?) {
        val w = (fbo?.width ?: 1).toFloat()
        val h = (fbo?.height ?: 1).toFloat()
        putVec4("viewportInfo", 1f / w, 1f / h, w, h)
        put("operationMode", pixelMode.type)
        put("edgeThreshold", edgeThreshold)
        put("edgeSharpness", edgeSharpness)
    }

    override fun getFragmentShader(): String {
      return loadFilterFromAsset("filters/super_resolution.frag")
    }
}

enum class SRPixelMode(val type: Int) {
    RGBA(1),
    RGBY(3),
    LERP(4)
}