package com.goog.effect.filter.core

import android.opengl.GLES20
import android.util.Log
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.utils.TAG
import kotlin.math.abs

/**
 * 用于实现降采样和升采样的Filter包装
 * [sample] 采样系数 1为原图
 * [upSample] 为true 为升采样 false 为降采样
 */
abstract class GLSampleFilter(sample: Float = 1f, val upSample: Boolean, iterationCount: Int = 1) : GLIteratorFilter(iterationCount) {

    protected var mSample = sample

    protected var originWidth = 0

    protected var originHeight = 0

    fun setSample(sample: Float) {
        if (sample == mSample) {
            return
        }
        mSample = sample
        markNeedUpdateArgs()
    }

    override fun setFrameSize(width: Int, height: Int) {
        originWidth = width
        originHeight = height
        val sample = mSample
        if (abs(sample - 1f) <= 0.00001 || sample <= 0.00001) {
            super.setFrameSize(width, height)
            return
        }
        val sampleFactor = if (upSample) {
            sample
        } else 1f / sample
        val w = (sampleFactor * width).toInt()
        val h = (sampleFactor * height).toInt()
        super.setFrameSize(w, h)
    }

    override fun onLastIterationPre(texName: Int, fbo: FrameBufferObject?) {
        super.onLastIterationPre(texName, fbo)
        GLES20.glViewport(0, 0, originWidth, originHeight)
    }

}

open class GLSampleWrapFilter(sample: Float = 1f, val upSample: Boolean, filter: GLFilter, iterationCount: Int = 1) :
    GLIteratorWrapFilter(filter, iterationCount) {

    protected var mSample = sample
    protected var originWidth = 0
    protected var originHeight = 0

    fun setSample(sample: Float) {
        if (sample == mSample) {
            return
        }
        mSample = sample
        markNeedUpdateArgs()
    }

    override fun setFrameSize(width: Int, height: Int) {
        originWidth = width
        originHeight = height
        if (mSample == 1f || mSample <= 0) {
            super.setFrameSize(width, height)
            return
        }
        val sampleFactor = if (upSample) {
            mSample
        } else 1f / mSample
        val w = (sampleFactor * width).toInt()
        val h = (sampleFactor * height).toInt()
        super.setFrameSize(w, h)
    }

    override fun onUpdateArgs() {
        release()
        initialize()
        setFrameSize(originWidth, originHeight)
    }

    override fun onLastIterationPre(texName: Int, fbo: FrameBufferObject?) {
        super.onLastIterationPre(texName, fbo)
        GLES20.glViewport(0, 0, originWidth, originHeight)
    }
}


