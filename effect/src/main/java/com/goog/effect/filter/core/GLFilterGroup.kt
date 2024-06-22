@file:Suppress("unused")

package com.goog.effect.filter.core

import android.opengl.GLES20
import android.util.Pair
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.CallBy

open class GLFilterGroup(filters: List<GLFilter>? = null) : GLBaseFilterGroup(filters) {

    private var mFboList: List<FrameBufferObject> = emptyList()

    constructor(vararg glFilters: GLFilter) : this(listOf<GLFilter>(*glFilters))

    override fun onInitialize(callBy: CallBy) {
        super.onInitialize(callBy)
        val filterList = mFilters
        for (item in filterList) {
            item.initialize(callBy)
        }

        mFboList = releaseFBOList(mFboList)
        if (filterList.size > 1) {
            mFboList = createFBOList(2, false)
        }
    }

    override fun setFrameSize(width: Int, height: Int) {
        super.setFrameSize(width, height)
        for (item in mFboList) {
            item.initialize(width, height)
        }
    }

    override fun onUpdateArgs() {
        super.onUpdateArgs()
        mFboList = releaseFBOList(mFboList)
        if (mFilters.size > 1) {
            mFboList = createFBOList(2, true)
        }
    }

    override fun release(callBy: CallBy) {
        mFboList = releaseFBOList(mFboList)
        super.release(callBy)
    }

    override fun draw(texName: Int, fbo: FrameBufferObject?) {
        val filterList = mFilters
        val fboList = mFboList

        if (filterList.size < 2 || fboList.size < 2) {
            filterList.firstOrNull()?.draw(texName, fbo)
            return
        }

        var curTexture = texName
        var curFBOIndex = 0
        for (i in 0 until filterList.size - 1) {
            val curFilter = filterList[i]
            val curFBO = fboList[curFBOIndex]
            curFBO.enable(true)
            curFilter.draw(curTexture, curFBO)
            curTexture = curFBO.texName
            curFBOIndex = 1 - curFBOIndex
        }

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
        fbo?.enable()
        filterList.lastOrNull()?.draw(curTexture, fbo)
    }

}

/**
 * 该类实现是通过创建过个FBO来实现的
 * 其性能不如[GLFilterGroup]的性能好
 */
open class GLFilterGroup2(filters: List<GLFilter>? = null) : GLBaseFilterGroup(filters) {
    private var list = listOf<Pair<GLFilter, FrameBufferObject?>>()

    init {
        if (filters != null) {
            mFilters = filters
        }
    }

    constructor(vararg glFilters: GLFilter) : this(listOf<GLFilter>(*glFilters))

    override fun onInitialize(callBy: CallBy) {
        super.onInitialize(callBy)
        val filters = mFilters
        val max = filters.size
        val list = mutableListOf<Pair<GLFilter, FrameBufferObject?>>()
        for ((index, shader) in filters.withIndex()) {
            shader.initialize(callBy)
            val fbo = if ((index + 1) < max) {
                FrameBufferObject()
            } else {
                null
            }
            list.add(Pair.create(shader, fbo))
        }
        this.list = list
    }

    override fun setFrameSize(width: Int, height: Int) {
        super.setFrameSize(width, height)
        for (pair in list) {
            pair.first.setFrameSize(width, height)
        }
    }

    override fun release(callBy: CallBy) {
        super.release(callBy)
        for (pair in list) {
            pair.second?.release()
        }
        list = listOf()
    }

    override fun draw(texName: Int, fbo: FrameBufferObject?) {
        var prevTexName = texName
        for (pair in list) {
            val curFBO = pair.second
            val curFilter = pair.first
            if (curFBO != null) {
                if (curFilter != null) {
                    curFBO.enable(true)
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

}


open class GLBaseFilterGroup(filters: List<GLFilter>? = null) : GLFilter() {
    protected var mFilters: List<GLFilter> = listOf()

    init {
        if (filters != null) {
            mFilters = filters.toList()
        }
    }

    open fun addFilter(filter: GLFilter) {
        val list = mFilters.toMutableList()
        list.add(filter)
        mFilters = list
        markNeedUpdateArgs()
    }

    fun addFilters(filterList: Collection<GLFilter>) {
        val list = mutableListOf<GLFilter>()
        list.addAll(mFilters)
        list.addAll(filterList)
        mFilters = list
        markNeedUpdateArgs()
    }

    open fun removeFilter(filter: GLFilter) {
        val list = mFilters.toMutableList()
        if (list.remove(filter)) {
            mFilters = list
            markNeedUpdateArgs()
        }
    }

    open fun removeAt(index: Int) {
        if (index < 0 || index >= mFilters.size) {
            return
        }
        val list = mFilters.toMutableList()
        list.removeAt(index)
        mFilters = list
        markNeedUpdateArgs()
    }

    open fun clear() {
        if (mFilters.isEmpty()) {
            return
        }
        mFilters = emptyList()
        markNeedUpdateArgs()
    }

    override fun setFrameSize(width: Int, height: Int) {
        super.setFrameSize(width, height)
        for (filter in mFilters) {
            filter.setFrameSize(width, height)
        }
    }

    override fun setEnable(enable: Boolean) {
        super.setEnable(enable)
        for (item in mFilters) {
            item.setEnable(enable)
        }
    }

    override fun onUpdateArgs() {
        for (item in mFilters) {
            item.initialize(CallBy.UPDATE_ARGS)
            item.setFrameSize(width, height)
        }
    }

    override fun release(callBy: CallBy) {
        for (item in mFilters) {
            item.release(callBy)
        }
        super.release(callBy)
    }

    override fun runTaskQueueIfNeed() {
        super.runTaskQueueIfNeed()
        for (filter in mFilters) {
            filter.runTaskQueueIfNeed()
        }
    }

}