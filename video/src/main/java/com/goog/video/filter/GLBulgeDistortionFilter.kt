package com.goog.video.filter

import com.goog.video.gl.EFrameBufferObject
import com.goog.video.utils.checkArgs

class GLBulgeDistortionFilter(cx: Float = 0.5f, cy: Float = 0.5f, r: Float = 0.25f, scale: Float = 0.5f) : GLFilter() {
    private var centerX: Float = 0.5f
    private var centerY: Float = 0.5f
    private var radius: Float = 0.25f
    private var scale: Float = 0.5f

    init {
        setCenter(cx, cy)
        setRadius(r)
        setScale(scale)
    }

    fun setCenter(cx: Float, cy: Float) {
        setCenterX(cx)
        setCenterY(cy)
    }

    fun setCenterX(cx: Float) {
        checkArgs(cx in 0.0f..1.0f)
        centerX = cx
    }

    fun setCenterY(cy: Float) {
        checkArgs(cy in 0.0f..1.0f)
        centerY = cy
    }

    fun setRadius(r: Float) {
        checkArgs(r >= 0)
        radius = r
    }

    fun setScale(s: Float) {
        checkArgs(s >= 0)
        scale = s
    }


    override fun onDraw(fbo: EFrameBufferObject?) {


        putVec2("center", centerX, centerY)
        put("radius",radius)
        put("scale", scale)
    }

    override fun getFragmentShader(): String {
        return """
            precision mediump float;
            varying highp vec2 vTextureCoord;
            uniform lowp sampler2D sTexture;
            uniform highp vec2 center;
            uniform highp float radius;
            uniform highp float scale;

            void main() {
                highp vec2 textureCoordinateToUse = vTextureCoord;
                highp float dist = distance(center, vTextureCoord);
                textureCoordinateToUse -= center;
                if (dist < radius) {
                    highp float percent = 1.0 - ((radius - dist) / radius) * scale;
                    percent = percent * percent;
                    textureCoordinateToUse = textureCoordinateToUse * percent;
                }
                textureCoordinateToUse += center;
                gl_FragColor = texture2D(sTexture, textureCoordinateToUse);
            }
        """.trimIndent()
    }
}
