package com.goog.effect.filter.core

import android.opengl.GLES20
import androidx.annotation.CallSuper
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.gl.GLConstant
import com.goog.effect.model.IntDelegate

/**
 * 用于实现需要进行多次迭代的Filter
 * 迭代之间存在强关联性
 */
abstract class GLIteratorFilter(iterationCount: Int = 1) : GLFilter() {
    protected var mIteratorCount by IntDelegate(iterationCount, 1)
    private val iterationInfo = IterationInfo()

    ///这里我们只创建两个FBO或者都不创建,对于多次迭代我们交替使用FBO来实现
    protected var mFboList: List<FrameBufferObject> = emptyList()

    fun setIteratorCount(count: Int) {
        if (count == mIteratorCount) {
            return
        }
        mIteratorCount = count
        markNeedUpdateArgs()
    }

    @CallSuper
    override fun onInitialize() {
        super.onInitialize()
        mFboList = releaseFBOList(mFboList)
        mFboList = if (mIteratorCount >= 2) {
            createFBOList(2, false)
        } else {
            emptyList()
        }
    }

    override fun release() {
        mFboList = releaseFBOList(mFboList)
        super.release()
    }

    override fun setFrameSize(width: Int, height: Int) {
        super.setFrameSize(width, height)
        for (fbo in mFboList) {
            fbo.initialize(width, height)
        }
    }

    override fun onUpdateArgs() {
        mFboList = releaseFBOList(mFboList)
        mFboList = if (mIteratorCount >= 2) {
            createFBOList(2, true)
        } else {
            emptyList()
        }
    }

    override fun draw(texName: Int, fbo: FrameBufferObject?) {
        val iterationCount = mIteratorCount
        val list = mFboList
        onIterationPre(iterationInfo)
        if (list.size < 2 || iterationCount <= 1) {
            onIteration(iterationInfo, 0)
            onLastIterationPre(texName, fbo)
            superDrawV2(texName, fbo, iterationInfo)
            return
        }

        var currentTexture: Int = texName
        for (i in 0 until iterationCount - 1) {
            val fboIndex = i % 2
            val tmpFBO = list[fboIndex]
            tmpFBO.enable()
            GLES20.glViewport(0, 0, width, height)
            onIteration(iterationInfo, i)
            superDrawV2(currentTexture, tmpFBO, iterationInfo)
            currentTexture = tmpFBO.texName
        }

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
        fbo?.enable()
        onIteration(iterationInfo, iterationCount)
        onLastIterationPre(currentTexture, fbo)
        superDrawV2(currentTexture, fbo, iterationInfo)
    }

    protected open fun superDrawV2(texName: Int, fbo: FrameBufferObject?, info: IterationInfo) {
        useProgram(texName, fbo)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferName)
        ///设置顶点Position
        GLES20.glEnableVertexAttribArray(getHandle(GLConstant.K_ATTR_POSITION))
        GLES20.glVertexAttribPointer(
            getHandle(GLConstant.K_ATTR_POSITION), VERTICES_DATA_POS_SIZE, GLES20.GL_FLOAT,
            false, VERTICES_DATA_STRIDE_BYTES, VERTICES_DATA_POS_OFFSET
        )
        ///设置坐标
        GLES20.glEnableVertexAttribArray(getHandle(GLConstant.K_ATTR_COORD))
        GLES20.glVertexAttribPointer(
            getHandle(GLConstant.K_ATTR_COORD), VERTICES_DATA_UV_SIZE, GLES20.GL_FLOAT,
            false, VERTICES_DATA_STRIDE_BYTES, VERTICES_DATA_UV_OFFSET
        )

        //激活并绑定到纹理0
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texName)
        //设置采样器(sampler2D)
        GLES20.glUniform1i(getHandle(GLConstant.K_UNIFORM_TEX), 0)
        onDraw2(fbo, info)
        onDrawEndHook(texName, fbo)
        ///绘制顶点数据
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
        ///禁用顶点着色器相关属性
        GLES20.glDisableVertexAttribArray(getHandle(GLConstant.K_ATTR_POSITION))
        GLES20.glDisableVertexAttribArray(getHandle(GLConstant.K_ATTR_COORD))
        //解绑纹理
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
    }

    ///在迭代前回调
    ///可以更新一些全局数据
    protected open fun onIterationPre(info: IterationInfo) {

    }

    ///在迭代中回调
    protected open fun onIteration(info: IterationInfo, iteratorIndex: Int) {
        info.iterationIndex = iteratorIndex
    }

    ///最后一次迭代结束前回调
    protected open fun onLastIterationPre(texName: Int, fbo: FrameBufferObject?) {

    }

    open fun onDraw2(fbo: FrameBufferObject?, info: IterationInfo) {}

    final override fun onDraw(fbo: FrameBufferObject?) {
        throw UnsupportedOperationException("Please use onDraw2")
    }

}

class IterationInfo {
    ///迭代索引
    var iterationIndex = -1
    var data: Any? = null
}

/**
 * 用于实现对单个Filter的迭代包装
 * 相对于GLGroupFilter 具有更好的性能
 */
open class GLIteratorWrapFilter(val filter: GLFilter, iterationCount: Int = 1) : GLFilter() {

    protected var mIterationCount by IntDelegate(iterationCount, 1)

    ///永远只有两个或者为空
    private var fboList = listOf<FrameBufferObject>()

    fun setIteratorCount(count: Int) {
        if (count == mIterationCount) {
            return
        }
        mIterationCount = count
        markNeedUpdateArgs()
    }

    override fun onUpdateArgs() {
        super.onUpdateArgs()
        if (mIterationCount >= 2) {
            val list = mutableListOf<FrameBufferObject>()
            for (i in 0..1) {
                val fbo = FrameBufferObject()
                fbo.initialize(width, height)
                list.add(fbo)
            }
            fboList = list
        } else {
            for (item in fboList) {
                item.release()
            }
            fboList = emptyList()
        }
    }

    override fun initialize() {
        super.initialize()
        val list = mutableListOf<FrameBufferObject>()
        for (i in 0..<1) {
            list.add(FrameBufferObject())
        }
        fboList = list
    }

    override fun onInitialize() {
        filter.initialize()
    }

    override fun release() {
        for (fbo in fboList) {
            fbo.release()
        }
        fboList = listOf()
        filter.release()
        handleMap.clear()
    }

    override fun setFrameSize(width: Int, height: Int) {
        super.setFrameSize(width, height)
        filter.setFrameSize(width, height)
        for (fbo in fboList) {
            fbo.initialize(width, height)
        }
    }

    override fun draw(texName: Int, fbo: FrameBufferObject?) {
        val list = fboList
        val count = mIterationCount
        if (count < 2 || list.size < 2) {
            onLastIterationPre(texName, fbo)
            filter.draw(texName, fbo)
            return
        }

        var prevTexName: Int = texName
        for (i in 0 until count - 1) {
            val fboIndex = i % 2
            val tmpFBO = list[fboIndex]
            tmpFBO.enable()
            filter.draw(prevTexName, tmpFBO)
            prevTexName = tmpFBO.texName
        }

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
        fbo?.enable()
        onLastIterationPre(prevTexName, fbo)
        filter.draw(prevTexName, fbo)

    }

    override fun setEnable(enable: Boolean) {
        super.setEnable(enable)
        filter.setEnable(enable)
    }

    ///最后一次迭代结束前回调
    protected open fun onLastIterationPre(texName: Int, fbo: FrameBufferObject?) {

    }


}
