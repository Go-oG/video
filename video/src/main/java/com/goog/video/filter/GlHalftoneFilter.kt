package com.goog.video.filter

import com.goog.video.gl.EFrameBufferObject
import com.goog.video.utils.checkArgs

///半色调滤镜
class GlHalftoneFilter(fraction: Float = 0.01f, aspectRatio: Float = 1f) : GlFilter() {
    private var fractionalWidthOfPixel = 0.01f
    private var aspectRatio = 1f

    init {
        setFractionalWidthOfAPixel(fraction)
        setAspectRatio(aspectRatio)
    }

    fun setAspectRatio(v: Float) {
        checkArgs(v > 0, "aspect ratio must be greater than 0")
        this.aspectRatio = v
    }

    fun setFractionalWidthOfAPixel(v: Float) {
        checkArgs(v > 0, "fractional width of a pixel must be greater than 0")
        this.fractionalWidthOfPixel = v
    }

    override fun setFrameSize(width: Int, height: Int) {
        super.setFrameSize(width, height)
        ///TODO: 写反了?
        aspectRatio = height.toFloat() / width.toFloat()
    }

    override fun onDraw(fbo: EFrameBufferObject?) {
        put("fractionalWidthOfPixel", fractionalWidthOfPixel)
        put("aspectRatio", aspectRatio)
    }

    override fun getFragmentShader(): String {
        return """
            precision mediump float;
            varying vec2 vTextureCoord;
            uniform lowp sampler2D sTexture;
            uniform highp float fractionalWidthOfPixel;
            uniform highp float aspectRatio;
            const highp vec3 W = vec3(0.2125, 0.7154, 0.0721);
            void main() {
                highp vec2 sampleDivisor = vec2(fractionalWidthOfPixel, fractionalWidthOfPixel / aspectRatio);
                highp vec2 samplePos = vTextureCoord - mod(vTextureCoord, sampleDivisor) + 0.5 * sampleDivisor;
                highp vec2 textureCoordinateToUse = vec2(vTextureCoord.x, (vTextureCoord.y * aspectRatio + 0.5 - 0.5 * aspectRatio));
                highp vec2 adjustedSamplePos = vec2(samplePos.x, (samplePos.y * aspectRatio + 0.5 - 0.5 * aspectRatio));
                highp float distanceFromSamplePoint = distance(adjustedSamplePos, textureCoordinateToUse);
                lowp vec3 sampledColor = texture2D(sTexture, samplePos).rgb;
                highp float dotScaling = 1.0 - dot(sampledColor, W);
                lowp float checkForPresenceWithinDot = 1.0 - step(distanceFromSamplePoint, (fractionalWidthOfPixel * 0.5) * dotScaling);
                gl_FragColor = vec4(vec3(checkForPresenceWithinDot), 1.0);
            }
        """.trimIndent()
    }
}
