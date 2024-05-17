package com.goog.video.filter

import android.opengl.GLES20
import android.util.Pair
import com.goog.video.gl.FrameBufferObject

open class GLFilterGroup(internal var filters: List<GLFilter>) : GLFilter() {
    private var list = listOf<Pair<GLFilter?, FrameBufferObject?>>()

    constructor(vararg glFilters: GLFilter) : this(listOf<GLFilter>(*glFilters))

    override fun setup() {
        super.setup()
        val max = filters.size
        val list = mutableListOf<Pair<GLFilter?, FrameBufferObject?>>()
        for ((count, shader) in filters.withIndex()) {
            shader.setup()
            val fbo = if ((count + 1) < max) {
                FrameBufferObject()
            } else {
                null
            }
            list.add(Pair.create(shader, fbo))
        }
        this.list = list

    }

    override fun release() {
        for (pair in list) {
            if (pair.first != null) {
                pair.first!!.release()
            }
            if (pair.second != null) {
                pair.second!!.release()
            }
        }
        list = listOf()
        super.release()
    }

    override fun setFrameSize(width: Int, height: Int) {
        super.setFrameSize(width, height)

        for (pair in list) {
            pair.first?.setFrameSize(width, height)
            pair.second?.setup(width, height)
        }
    }

    private var prevTexName = 0

    override fun draw(texName: Int, fbo: FrameBufferObject?) {
        prevTexName = texName
        for (pair in list) {
            val second = pair.second
            val first = pair.first
            if (second != null) {
                if (first != null) {
                    second.enable()
                    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
                    first.draw(prevTexName, second)
                }
                prevTexName = second.texName
            } else {
                if (fbo != null) {
                    fbo.enable()
                } else {
                    GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
                }
                first?.draw(prevTexName, fbo)
            }
        }
    }

}
