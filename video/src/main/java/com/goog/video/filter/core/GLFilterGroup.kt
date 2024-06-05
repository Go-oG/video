package com.goog.video.filter.core

import android.opengl.GLES20
import android.util.Pair
import com.goog.video.gl.FrameBufferObject

open class GLFilterGroup(internal var filters: List<GLFilter>) : GLFilter() {
    private var list = listOf<Pair<GLFilter, FrameBufferObject?>>()

    constructor(vararg glFilters: GLFilter) : this(listOf<GLFilter>(*glFilters))

    override fun initialize() {
        super.initialize()
        val max = filters.size
        val list = mutableListOf<Pair<GLFilter, FrameBufferObject?>>()
        for ((index, shader) in filters.withIndex()) {
            shader.initialize()
            val fbo = if ((index + 1) < max) {
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
            pair.first?.release()
            pair.second?.release()
        }
        list = listOf()
        super.release()
    }

    override fun setFrameSize(width: Int, height: Int) {
        super.setFrameSize(width, height)

        for (pair in list) {
            pair.first.setFrameSize(width, height)
            pair.second?.initialize(width, height)
        }
    }

    override fun draw(texName: Int, fbo: FrameBufferObject?) {
        var prevTexName = texName
        for (pair in list) {
            val second = pair.second
            val first = pair.first
            if (second != null) {
                if (first != null) {
                    second.enable()
                    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
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
