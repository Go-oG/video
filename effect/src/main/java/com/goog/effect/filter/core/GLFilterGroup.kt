package com.goog.effect.filter.core

import android.opengl.GLES20
import android.util.Pair
import com.goog.effect.gl.FrameBufferObject

open class GLFilterGroup(filters: List<GLFilter>? = null) : GLFilter() {
    private var list = listOf<Pair<GLFilter, FrameBufferObject?>>()

    protected var mFilters: List<GLFilter> = listOf()

    init {
        if (filters != null) {
            mFilters = filters
        }
    }

    constructor(vararg glFilters: GLFilter) : this(listOf<GLFilter>(*glFilters))

    override fun onInitialize() {
        super.onInitialize()
        val filters = mFilters
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

    override fun setEnable(enable: Boolean) {
        super.setEnable(enable)
        for (item in list) {
            item.first.setEnable(enable)
        }
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
            val curFBO = pair.second
            val curFilter = pair.first
            if (curFBO != null) {
                if (curFilter != null) {
                    curFBO.enable()
                    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
                    curFilter.draw(prevTexName, curFBO)
                }
                prevTexName = curFBO.texName
            } else {
                if (fbo != null) {
                    fbo.enable()
                } else {
                    GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
                }
                curFilter?.draw(prevTexName, fbo)
            }
        }
    }

    override fun runTaskQueue() {
        super.runTaskQueue()
        for (filter in list) {
            filter.first.runTaskQueue()
        }
    }
}
