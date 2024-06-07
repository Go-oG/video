package com.goog.effect.filter.core

import android.opengl.GLES11Ext
import android.opengl.GLES20
import com.goog.effect.gl.GLConstant.DEF_FRAGMENT_SHADER
import com.goog.effect.gl.GLConstant.K_ATTR_COORD
import com.goog.effect.gl.GLConstant.K_ATTR_POSITION
import com.goog.effect.gl.GLConstant.K_UNIFORM_TEX

/**
 * [texTarget] 是纹理目标类型
 * 这里传入的是 [GLES11Ext.GL_TEXTURE_EXTERNAL_OES]
 */
class GLPreviewFilter(private val texTarget: Int) : GLFilter() {

    /**
     *这里外部传入的texName是oes对应的纹理ID
     */
    fun draw(texName: Int, mvpMatrix: FloatArray, stMatrix: FloatArray, aspectRatio: Float) {
        useProgram(texName,null)

        GLES20.glUniformMatrix4fv(getHandle("uMVPMatrix"), 1, false, mvpMatrix, 0)
        GLES20.glUniformMatrix4fv(getHandle("uSTMatrix"), 1, false, stMatrix, 0)
        GLES20.glUniform1f(getHandle("uCRatio"), aspectRatio)

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferName)
        GLES20.glEnableVertexAttribArray(getHandle(K_ATTR_POSITION))
        GLES20.glVertexAttribPointer(getHandle(K_ATTR_POSITION), VERTICES_DATA_POS_SIZE, GLES20.GL_FLOAT, false,
                VERTICES_DATA_STRIDE_BYTES, VERTICES_DATA_POS_OFFSET)

        GLES20.glEnableVertexAttribArray(getHandle(K_ATTR_COORD))
        GLES20.glVertexAttribPointer(getHandle(K_ATTR_COORD), VERTICES_DATA_UV_SIZE, GLES20.GL_FLOAT, false,
                VERTICES_DATA_STRIDE_BYTES, VERTICES_DATA_UV_OFFSET)

        //激活并绑定一个纹理单元
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(texTarget, texName)
        GLES20.glUniform1i(getHandle(K_UNIFORM_TEX), 0)

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

        GLES20.glDisableVertexAttribArray(getHandle(K_ATTR_POSITION))
        GLES20.glDisableVertexAttribArray(getHandle(K_UNIFORM_TEX))
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
        GLES20.glBindTexture(texTarget, 0)

    }

    override fun getVertexShader(): String {
        return """
            uniform mat4 uMVPMatrix;
            uniform mat4 uSTMatrix;
            uniform float uCRatio;
            attribute vec4 aPosition;
            attribute vec4 aTextureCoord;
            varying highp vec2 vTextureCoord;
            void main() {
                vec4 scaledPos = aPosition;
                scaledPos.x = scaledPos.x * uCRatio;
                gl_Position = uMVPMatrix * scaledPos;
                vTextureCoord = (uSTMatrix * aTextureCoord).xy;
            }
        """.trimIndent()
    }

    override fun getFragmentShader(): String {
        if (texTarget == GLES11Ext.GL_TEXTURE_EXTERNAL_OES) {
            return StringBuilder()
                .append("#extension GL_OES_EGL_image_external : require\n")
                .append(DEF_FRAGMENT_SHADER.replace("sampler2D", "samplerExternalOES"))
                .toString()
        }
        return DEF_FRAGMENT_SHADER
    }

}
