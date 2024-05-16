package com.goog.video.filter

import android.opengl.GLES20

class GLPreviewFilter(private val texTarget: Int) : GLFilter() {

    fun draw(texName: Int, mvpMatrix: FloatArray?, stMatrix: FloatArray?, aspectRatio: Float) {
        useProgram()

        GLES20.glUniformMatrix4fv(getHandle("uMVPMatrix"), 1, false, mvpMatrix, 0)
        GLES20.glUniformMatrix4fv(getHandle("uSTMatrix"), 1, false, stMatrix, 0)
        GLES20.glUniform1f(getHandle("uCRatio"), aspectRatio)

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferName)
        GLES20.glEnableVertexAttribArray(getHandle(K_ATTR_POSITION))
        GLES20.glVertexAttribPointer(getHandle(K_ATTR_POSITION), VERTICES_DATA_POS_SIZE, GLES20.GL_FLOAT, false,
                VERTICES_DATA_STRIDE_BYTES, VERTICES_DATA_POS_OFFSET)

        GLES20.glEnableVertexAttribArray(getHandle(K_ATTR_TEXTURE_COORD))
        GLES20.glVertexAttribPointer(getHandle(K_ATTR_TEXTURE_COORD), VERTICES_DATA_UV_SIZE, GLES20.GL_FLOAT, false,
                VERTICES_DATA_STRIDE_BYTES, VERTICES_DATA_UV_OFFSET)

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(texTarget, texName)
        GLES20.glUniform1i(getHandle(K_UNIFORM_SAMPLER2D), 0)

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

        GLES20.glDisableVertexAttribArray(getHandle(K_ATTR_POSITION))
        GLES20.glDisableVertexAttribArray(getHandle(K_ATTR_TEXTURE_COORD))
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
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
        if (texTarget == GL_TEXTURE_EXTERNAL_OES) {
            return StringBuilder()
                .append("#extension GL_OES_EGL_image_external : require\n")
                .append(DEFAULT_FRAGMENT_SHADER.replace("sampler2D", "samplerExternalOES"))
                .toString()
        }
        return DEFAULT_FRAGMENT_SHADER
    }

    companion object {
        const val GL_TEXTURE_EXTERNAL_OES: Int = 0x8D65
    }
}
