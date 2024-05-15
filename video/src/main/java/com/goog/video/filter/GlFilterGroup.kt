package com.goog.video.filter

import android.opengl.GLES20
import android.util.Pair
import com.goog.video.gl.EFrameBufferObject
import java.util.Arrays

class GlFilterGroup(private val filters: Collection<GlFilter>) : GlFilter() {
    private val list = ArrayList<Pair<GlFilter?, EFrameBufferObject?>>()

    constructor(vararg glFilters: GlFilter) : this(listOf<GlFilter>(*glFilters))

    override fun setup() {
        super.setup()
        val max = filters.size
        for ((count, shader) in filters.withIndex()) {
            shader.setup()
            val fbo = if ((count + 1) < max) {
                EFrameBufferObject()
            } else {
                null
            }
            list.add(Pair.create(shader, fbo))
        }

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
        list.clear()
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

    override fun draw(texName: Int, fbo: EFrameBufferObject?) {
        prevTexName = texName
        for (pair in list) {
            val second = pair.second
            val first = pair.first
            if (second != null) {
                if (first != null) {
                    second.enable()
                    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
                    first.draw(prevTexName, pair.second)
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
