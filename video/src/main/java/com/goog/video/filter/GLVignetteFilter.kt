package com.goog.video.filter

import com.goog.video.gl.EFrameBufferObject
import com.goog.video.utils.checkArgs

class GLVignetteFilter(cx: Float = 0.5f, cy: Float = 0.5f, start: Float = 0.5f, end: Float = 0.75f) : GLFilter() {
    private var centerX = 0.5f
    private var centerY = 0.5f
    private var vignetteStart: Float = 0.5f
    private var vignetteEnd: Float = 0.75f

    init {
        setCenter(cx, cy)
        setVignetteStart(start)
        setVignetteEnd(end)
    }

    fun setCenter(centerX: Float, centerY: Float) {
        setCenterX(centerX)
        setCenterY(centerY)
    }

    fun setCenterX(v: Float) {
        checkArgs(v in 0f..1f)
        this.centerX = v
    }

    fun setCenterY(v: Float) {
        checkArgs(v in 0f..1f)
        this.centerY = v
    }

    fun setVignetteStart(v: Float) {
        checkArgs(v >= 0)
        this.vignetteStart = v
    }

    fun setVignetteEnd(v: Float) {
        checkArgs(v >= 0)
        this.vignetteEnd = v
    }


    override fun onDraw(fbo: EFrameBufferObject?) {
        putVec2("vignetteCenter", centerX, centerY)
        put("vignetteStart", vignetteStart)
        put("vignetteEnd", vignetteEnd)
    }

    override fun getFragmentShader(): String {
        return """
            precision mediump float;
            varying vec2 vTextureCoord;
            uniform lowp sampler2D sTexture;
            uniform lowp vec2 vignetteCenter;
            uniform highp float vignetteStart;
            uniform highp float vignetteEnd;
            void main() {
                lowp vec3 rgb = texture2D(sTexture, vTextureCoord).rgb;
                lowp float d = distance(vTextureCoord, vec2(vignetteCenter.x, vignetteCenter.y));
                lowp float percent = smoothstep(vignetteStart, vignetteEnd, d);
                gl_FragColor = vec4(mix(rgb.x, 0.0, percent), mix(rgb.y, 0.0, percent), mix(rgb.z, 0.0, percent), 1.0);
            }
        """.trimIndent()
    }
}
