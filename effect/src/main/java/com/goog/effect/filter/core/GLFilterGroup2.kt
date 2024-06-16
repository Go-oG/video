package com.goog.effect.filter.core

import android.opengl.GLES20
import com.goog.effect.gl.FrameBufferObject

/**
 * 目前该类和[GLIteratorFilter]同时使用时会有冲突
 */
open class GLFilterGroup2(filters: List<GLFilter>? = null) : GLFilter() {

    protected var mFilters: List<GLFilter> = listOf()

    private var mFboList: List<FrameBufferObject> = emptyList()

    init {
        if (filters != null) {
            mFilters = filters.toList()
        }
    }

    constructor(vararg glFilters: GLFilter) : this(listOf<GLFilter>(*glFilters))

    fun addFilter(filter: GLFilter) {
        val list = mFilters.toMutableList()
        list.add(filter)
        mFilters = list
        markNeedUpdateArgs()
    }

    fun removeFilter(filter: GLFilter) {
        val list = mFilters.toMutableList()
        list.remove(filter)
        mFilters = list
        markNeedUpdateArgs()
    }

    fun removeAt(index: Int) {
        val list = mFilters.toMutableList()
        if (list.size > index) {
            list.removeAt(index)
            mFilters = list
            markNeedUpdateArgs()
        }
    }

    override fun onInitialize() {
        super.onInitialize()
        val filterList = mFilters
        for (item in filterList) {
            item.initialize()
        }
        mFboList = releaseFBOList(mFboList)
        if (filterList.size > 1) {
            mFboList = createFBOList(2, false)
        }
    }

    override fun setFrameSize(width: Int, height: Int) {
        super.setFrameSize(width, height)
        for (item in mFilters) {
            item.setFrameSize(width, height)
        }
        for (item in mFboList) {
            item.initialize(width, height)
        }
    }

    override fun onUpdateArgs() {
        for (item in mFilters) {
            item.initialize()
            item.setFrameSize(width, height)
        }

        mFboList = releaseFBOList(mFboList)
        if (mFilters.size > 1) {
            mFboList = createFBOList(2, true)
        }
    }

    override fun release() {
        for (item in mFilters) {
            item.release()
        }
        mFboList = releaseFBOList(mFboList)
        super.release()
    }

    override fun setEnable(enable: Boolean) {
        super.setEnable(enable)
        for (item in mFilters) {
            item.setEnable(enable)
        }
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
            curFBO.enable()
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
            curFilter.draw(curTexture, curFBO)
            curTexture = curFBO.texName
            curFBOIndex = 1 - curFBOIndex
        }

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
        fbo?.enable()
        filterList.lastOrNull()?.draw(curTexture, fbo)
    }

    override fun runTaskQueueIfNeed() {
        super.runTaskQueueIfNeed()
        for (filter in mFilters) {
            filter.runTaskQueueIfNeed()
        }
    }


}
