package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.utils.loadFilterFromAsset

open class GLTransformFilter : GLFilter() {
    private var transformMatrix: FloatArray = FloatArray(0)
    private var orthographicMatrix: FloatArray = FloatArray(0)

    fun setTransformMatrix(v: FloatArray) {
        transformMatrix = v
    }

    fun setOrthographicMatrix(v: FloatArray) {
        orthographicMatrix = v
    }

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        putMatrix4("transformMatrix", transformMatrix)
        putMatrix4("orthographicMatrix", orthographicMatrix)

    }

    override fun getVertexShader(): String {
        return loadFilterFromAsset("transform.vsh")
    }
}