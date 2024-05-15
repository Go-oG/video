package com.goog.video.filter

import com.goog.video.gl.EFrameBufferObject
import com.goog.video.utils.checkArgs

class GlSphereRefractionFilter(cx: Float = 0.5f, cy: Float = 0.5f, radius: Float = 0.5f,
    ratio: Float = 0.5f, refractiveIndex: Float = 0.71f) : GlFilter() {
    private var centerX = 0.5f
    private var centerY = 0.5f
    private var radius = 0.5f
    private var aspectRatio = 1.0f
    private var refractiveIndex = 0.71f

    init {
        setCenterX(cx)
        setCenterY(cy)
        setRadius(radius)
        setAspectRatio(ratio)
        setRefractiveIndex(refractiveIndex)
    }

    fun setCenterX(v: Float) {
        checkArgs(v in 0.0..1.0, "centerX must be in the range [0, 1]")
        this.centerX = v
    }

    fun setCenterY(v: Float) {
        checkArgs(v in 0.0..1.0, "centerY must be in the range [0, 1]")
        this.centerY = v
    }

    fun setRadius(v: Float) {
        checkArgs(v in 0.0..1.0, "radius must be in the range [0, 1]")
        this.radius = v
    }

    fun setAspectRatio(v: Float) {
        checkArgs(v > 0.0, "aspectRatio must be greater than 0")
        this.aspectRatio = v
    }

    fun setRefractiveIndex(v: Float) {
        checkArgs(v > 0.0, "refractiveIndex must be greater than 0")
        this.refractiveIndex = v
    }

    override fun onDraw(fbo: EFrameBufferObject?) {
        putVec2("center", centerX, centerY)
        put("radius", radius)
        put("aspectRatio", aspectRatio)
        put("refractiveIndex", refractiveIndex)
    }

    override fun getFragmentShader(): String {
        return """
            precision mediump float;
            varying vec2 vTextureCoord;
            
            uniform lowp sampler2D sTexture;
            uniform highp vec2 center;
            uniform highp float radius;
            uniform highp float aspectRatio;
            uniform highp float refractiveIndex;

            void main() {
                highp vec2 textureCoordinateToUse = vec2(vTextureCoord.x, (vTextureCoord.y * aspectRatio + 0.5 - 0.5 * aspectRatio));
                highp float distanceFromCenter = distance(center, textureCoordinateToUse);
                lowp float checkForPresenceWithinSphere = step(distanceFromCenter, radius);
                distanceFromCenter = distanceFromCenter / radius;
                highp float normalizedDepth = radius * sqrt(1.0 - distanceFromCenter * distanceFromCenter);
                highp vec3 sphereNormal = normalize(vec3(textureCoordinateToUse - center, normalizedDepth));
                highp vec3 refractedVector = refract(vec3(0.0, 0.0, -1.0), sphereNormal, refractiveIndex);
                gl_FragColor = texture2D(sTexture, (refractedVector.xy + 1.0) * 0.5) * checkForPresenceWithinSphere;
            }
        """.trimIndent()
    }
}
