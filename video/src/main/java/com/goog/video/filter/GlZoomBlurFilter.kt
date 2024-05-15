package com.goog.video.filter

import android.graphics.PointF
import com.goog.video.gl.EFrameBufferObject

class GlZoomBlurFilter : GlFilter() {
    private var blurCenter = PointF(0.5f, 0.5f)
    private var blurSize = 1f

    fun setBlurCenter(blurCenter: PointF) {
        this.blurCenter = blurCenter
    }

    fun setBlurSize(blurSize: Float) {
        this.blurSize = blurSize
    }

    override fun onDraw(fbo: EFrameBufferObject?) {
        putVec2("blurCenter", blurCenter.x, blurCenter.y)
        put("blurSize", blurSize)
    }

    override fun getFragmentShader(): String {
        return """
            precision mediump float;
            varying vec2 vTextureCoord;

            uniform lowp sampler2D sTexture;
            uniform highp vec2 blurCenter;
            uniform highp float blurSize;

            void main() {
                //TODO: Do a more intelligent scaling based on resolution here
                highp vec2 samplingOffset = 1.0 / 100.0 * (blurCenter - vTextureCoord) * blurSize;

                lowp vec4 fragmentColor = texture2D(sTexture, vTextureCoord) * 0.18;
                fragmentColor += texture2D(sTexture, vTextureCoord + samplingOffset) * 0.15;
                fragmentColor += texture2D(sTexture, vTextureCoord + (2.0 * samplingOffset)) * 0.12;
                fragmentColor += texture2D(sTexture, vTextureCoord + (3.0 * samplingOffset)) * 0.09;
                fragmentColor += texture2D(sTexture, vTextureCoord + (4.0 * samplingOffset)) * 0.05;
                fragmentColor += texture2D(sTexture, vTextureCoord - samplingOffset) * 0.15;
                fragmentColor += texture2D(sTexture, vTextureCoord - (2.0 * samplingOffset)) * 0.12;
                fragmentColor += texture2D(sTexture, vTextureCoord - (3.0 * samplingOffset)) * 0.09;
                fragmentColor += texture2D(sTexture, vTextureCoord - (4.0 * samplingOffset)) * 0.05;

                gl_FragColor = fragmentColor;
            }
        """.trimIndent()
    }
}
