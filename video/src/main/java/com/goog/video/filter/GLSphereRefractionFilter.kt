package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.model.FloatDelegate
import com.goog.video.utils.checkArgs

class GLSphereRefractionFilter : GLFilter() {

    var centerX by FloatDelegate(0.5f, 0f, 1f)
    var centerY by FloatDelegate(0.5f, 0f, 1f)
    var radius by FloatDelegate(0.5f, 0f, 1f)
    var aspectRatio by FloatDelegate(1f, 0f, 1f, includeMin = false)
    var refractiveIndex by FloatDelegate(0.71f, 0f, 1f, includeMin = false)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
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
