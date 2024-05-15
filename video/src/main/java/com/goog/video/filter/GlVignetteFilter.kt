package com.goog.video.filter

import com.goog.video.gl.EFrameBufferObject

class GlVignetteFilter(cx: Float = 0.5f, cy: Float = 0.5f, start: Float = 0.2f, end: Float = 0.85f) : GlFilter() {
    private var centerX = 0.5f
    private var centerY = 0.5f
    private var vignetteStart: Float = 0.2f
    private var vignetteEnd: Float = 0.85f

    init {
        setCenter(cx, cy)
        setVignetteStart(start)
        setVignetteEnd(end)
    }

    fun setCenter(centerX: Float, centerY: Float) {
        setCenterX(centerX)
        setCenterY(centerY)
    }

    fun setCenterX(centerX: Float) {
        this.centerX = centerX
    }

    fun setCenterY(centerY: Float) {
        this.centerY = centerY
    }

    fun setVignetteStart(vignetteStart: Float) {
        this.vignetteStart = vignetteStart
    }

    fun setVignetteEnd(v: Float) {
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
