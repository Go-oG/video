package com.goog.video.filter

import com.goog.video.gl.FrameBufferObject
import com.goog.video.utils.checkArgs

///TODO 待校验
class GLCornerFilter(corner: Float = 0f) : GLFilter() {
    private var topLeftRadius = 0.0f
    private var topRightRadius = 0.0f
    private var bottomLeftRadius = 0.0f
    private var bottomRightRadius = 0.0f

    init {
        setCorner(corner)
    }

    fun setCorner(corner: Float) {
        checkArgs(corner >= 0.0f, "corner must be >= 0")
        topRightRadius = corner
        topLeftRadius = corner
        bottomRightRadius = corner
        bottomLeftRadius = corner
    }

    fun setTopLeftCorner(corner: Float) {
        checkArgs(corner >= 0.0f, "topLeftRadius must be >= 0")
        this.topLeftRadius = corner
    }

    fun setTopRightCorner(corner: Float) {
        checkArgs(corner >= 0.0f, "topRightRadius must be >= 0")
        this.topRightRadius = corner
    }

    fun setBottomLeftCorner(corner: Float) {
        checkArgs(corner >= 0.0f, "bottomLeftRadius must be >= 0")
        this.bottomLeftRadius = corner
    }

    fun setBottomRightCorner(corner: Float) {
        checkArgs(corner >= 0.0f, "bottomRightRadius must be >= 0")
        this.bottomRightRadius = corner
    }

    override fun onDraw(fbo: FrameBufferObject?) {
        put("topLeftRadius", topLeftRadius)
        put("topRightRadius", topRightRadius)
        put("bottomLeftRadius", bottomLeftRadius)
        put("bottomRightRadius", bottomRightRadius)
        val w = fbo?.width ?: 1
        val h = fbo?.height ?: 1
        putVec2("mResolution", w.toFloat(), h.toFloat())
    }

    override fun getFragmentShader(): String {
        return """
            precision mediump float;
            varying highp vec2 vTextureCoord;
            uniform sampler2D sTexture;
            uniform float topLeftRadius;
            uniform float topRightRadius;
            uniform float bottomLeftRadius;
            uniform float bottomRightRadius;
            uniform vec2 mResolution;

            const vec2 tl = vec2(0.0, 1.0);
            const vec2 tr = vec2(1.0, 1.0);
            const vec2 bl = vec2(0.0, 0.0);
            const vec2 br = vec2(1.0, 0.0);

            void main() {
                vec2 coord = gl_FragCoord.xy / mResolution;
                vec4 color = texture2D(sTexture, vTextureCoord);

                float distTopLeft = distance(coord, tl) * mResolution.y;
                float distTopRight = distance(coord, tr) * mResolution.y;
                float distBottomLeft = distance(coord, bl) * mResolution.y;
                float distBottomRight = distance(coord, br) * mResolution.y;

                if (distTopLeft > topLeftRadius &&
                distTopRight > topRightRadius &&
                distBottomLeft > bottomLeftRadius &&
                distBottomRight > bottomRightRadius) {
                    discard;
                } else {
                    gl_FragColor = color;
                }
            }
        """.trimIndent()
    }
}