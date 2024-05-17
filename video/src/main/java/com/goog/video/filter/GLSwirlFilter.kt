package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.utils.checkArgs

class GLSwirlFilter(angle: Float = 1f, radius: Float = 0.5f, cx: Float = 0.5f, cy: Float = 0.5f) : GLFilter() {
    private var angle = 1.0f
    private var radius = 0.5f
    private var centerX = 0.5f
    private var centerY = 0.5f

    init {
        setAngle(angle)
        setRadius(radius)
        setCenterX(cx)
        setCenterY(cy)
    }

    fun setAngle(v: Float) {
        checkArgs(v>=0)
        this.angle = v
    }

    fun setRadius(radius: Float) {
        checkArgs(radius>=0)
        this.radius = radius
    }

    fun setCenterX(v: Float) {
        checkArgs(v in 0.0..1.0, "centerX must be in the range [0, 1]")
        this.centerX = v
    }

    fun setCenterY(v: Float) {
        checkArgs(v in 0.0..1.0, "centerY must be in the range [0, 1]")
        this.centerY = v
    }


    override fun onDraw(fbo: FrameBufferObject?) {
        putVec2("center", centerX, centerY)
        put("radius", radius)
        put("angle", angle)
    }

    override fun getFragmentShader(): String {
        return """
            precision mediump float;
            varying vec2 vTextureCoord;

            uniform lowp sampler2D sTexture;
            uniform highp vec2 center;
            uniform highp float radius;
            uniform highp float angle;

            void main() {
                highp vec2 textureCoordinateToUse = vTextureCoord;
                highp float dist = distance(center, vTextureCoord);
                if (dist < radius) {
                    textureCoordinateToUse -= center;
                    highp float percent = (radius - dist) / radius;
                    highp float theta = percent * percent * angle * 8.0;
                    highp float s = sin(theta);
                    highp float c = cos(theta);
                    textureCoordinateToUse = vec2(dot(textureCoordinateToUse, vec2(c, -s)), dot(textureCoordinateToUse, vec2(s, c)));
                    textureCoordinateToUse += center;
                }
                gl_FragColor = texture2D(sTexture, textureCoordinateToUse);
            }
        """.trimIndent()
    }
}
