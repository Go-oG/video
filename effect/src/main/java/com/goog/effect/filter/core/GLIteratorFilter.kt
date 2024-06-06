package com.goog.effect.filter.core

import android.opengl.GLES20
import androidx.annotation.CallSuper
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.gl.GLConstant
import com.goog.effect.model.IntDelegate
import java.util.concurrent.atomic.AtomicBoolean

///用于实现需要进行多次迭代的相同效果的Filter
open class GLIteratorFilter : GLFilter() {

    protected var iteratorCount by IntDelegate(1, 1)
    private val updateFlag = AtomicBoolean(true)

    private val iteratorInfo = IteratorInfo()
    private var fboList = listOf<FrameBufferObject>()

    @CallSuper
    override fun initialize() {
        super.initialize()
        updateFBOListIfNeed(iteratorCount)
    }

    fun setIteratorCount(count: Int) {
        if (count == iteratorCount) {
            return
        }
        iteratorCount = count
        updateFlag.set(true)
    }

    private fun updateFBOListIfNeed(size: Int) {
        val b2 = fboList.size != iteratorCount - 1 && iteratorCount > 1
        if (updateFlag.compareAndSet(true, false) || b2) {
            updateFlag.set(false)
            if (size <= 1) {
                fboList = listOf()
                return
            }
            val list = mutableListOf<FrameBufferObject>()
            for (i in 0..<size - 1) {
                list.add(FrameBufferObject())
            }
            for (item in list) {
                item.initialize(width, height)
            }
            fboList = list
        }
    }

    final override fun draw(texName: Int, fbo: FrameBufferObject?) {
        val count = iteratorCount
        updateFBOListIfNeed(count)

        val list = fboList
        onIteratorPre(iteratorInfo)
        ///迭代次数为1 则直接调用自身
        if (list.isEmpty() || count <= 1) {
            onIteration(iteratorInfo, 0)
            superDrawV2(texName, fbo, iteratorInfo)
            return
        }

        var prevTexName = texName
        for ((i, fboItem) in list.withIndex()) {
            onIteration(iteratorInfo, i)
            fboItem.enable()
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
            superDrawV2(texName, fbo, iteratorInfo)
            prevTexName = fboItem.texName
        }

        if (fbo != null) {
            fbo.enable()
        } else {
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
        }
        onIteration(iteratorInfo, list.size)
        superDrawV2(prevTexName, fbo, iteratorInfo)
    }

    private fun superDrawV2(texName: Int, fbo: FrameBufferObject?, info: IteratorInfo) {
        useProgram(texName, fbo)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferName)
        ///设置顶点Position
        GLES20.glEnableVertexAttribArray(getHandle(GLConstant.K_ATTR_POSITION))
        GLES20.glVertexAttribPointer(getHandle(GLConstant.K_ATTR_POSITION), VERTICES_DATA_POS_SIZE, GLES20.GL_FLOAT,
                false, VERTICES_DATA_STRIDE_BYTES, VERTICES_DATA_POS_OFFSET)
        ///设置坐标
        GLES20.glEnableVertexAttribArray(getHandle(GLConstant.K_ATTR_COORD))
        GLES20.glVertexAttribPointer(getHandle(GLConstant.K_ATTR_COORD), VERTICES_DATA_UV_SIZE, GLES20.GL_FLOAT,
                false, VERTICES_DATA_STRIDE_BYTES, VERTICES_DATA_UV_OFFSET)
        //激活并绑定纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texName)
        //设置纹理单元(sampler2D)
        GLES20.glUniform1i(getHandle(GLConstant.K_UNIFORM_TEX), 0)

        onDraw2(fbo, info)

        ///绘制顶点数据
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
        onDrawEndHook(texName, fbo)
        ///禁用顶点着色器相关属性
        GLES20.glDisableVertexAttribArray(getHandle(GLConstant.K_ATTR_POSITION))
        GLES20.glDisableVertexAttribArray(getHandle(GLConstant.K_ATTR_COORD))
        //解绑纹理
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
    }

    ///在迭代前回调
    ///可以更新一些全局数据
    protected open fun onIteratorPre(info: IteratorInfo) {

    }

    ///在迭代中回调
    protected open fun onIteration(info: IteratorInfo, iteratorIndex: Int) {
        info.iteratorIndex = iteratorIndex

    }

    open fun onDraw2(fbo: FrameBufferObject?, info: IteratorInfo) {}

    final override fun onDraw(fbo: FrameBufferObject?) {
        throw UnsupportedOperationException("Please use onDraw2")
    }

}

class IteratorInfo {
    var iteratorIndex = -1
    var data: Any? = null
}