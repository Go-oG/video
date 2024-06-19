package com.goog.effect.filter

import android.opengl.Matrix
import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.utils.checkArgs
import com.goog.effect.utils.loadFilterFromAsset

open class GLTransformFilter : GLFilter() {
    private var transformMatrix: FloatArray = FloatArray(16)
    private var orthographicMatrix: FloatArray = FloatArray(16)

    init {
        Matrix.setIdentityM(transformMatrix, 0)
        Matrix.setIdentityM(orthographicMatrix, 0)
    }

    fun setTransformMatrix(v: FloatArray) {
        checkArgs(v.size == 16)
        transformMatrix = v
    }

    fun setOrthographicMatrix(v: FloatArray) {
        checkArgs(v.size == 16)
        orthographicMatrix = v
    }

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        putMatrix4("uTransformMatrix", transformMatrix)
        putMatrix4("uOrthographicMatrix", orthographicMatrix)

    }

    override fun getVertexShader(): String {
        return loadFilterFromAsset("filters/transform.vert")
    }
}