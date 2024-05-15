package com.goog.video.filter

import android.graphics.PointF
import android.opengl.GLES20
import com.goog.video.gl.EFrameBufferObject

class GlSwirlFilter : GlFilter() {
    private var angle = 1.0f
    private var radius = 0.5f
    private var center = PointF(0.5f, 0.5f)

    fun setAngle(angle: Float) {
        this.angle = angle
    }

    fun setRadius(radius: Float) {
        this.radius = radius
    }

    fun setCenter(center: PointF) {
        this.center = center
    }

    override fun onDraw(fbo: EFrameBufferObject?) {
        put("center", center.x,center.y)
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
