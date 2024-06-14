package com.goog.effect.filter.blur

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.filter.core.GLIteratorFilter
import com.goog.effect.filter.core.IteratorInfo
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import kotlin.math.sin

//方向模糊
class GLDirectionBlurFilter : GLFilter() {
    //距离角度偏移量
    private var directionX = 0f
    private var directionY = 0f
    var strength by FloatDelegate(0.05f, 0.00001f)

    ///不能出现负数
    var samples = 10

    init {
        setAngle(45f)
    }

    fun setAngle(angle: Float) {
        setAngleX(angle)
        setAngleY(angle)
    }

    fun setAngleX(angle: Float) {
        directionX = sin(Math.toRadians(angle.toDouble())).toFloat()
    }

    fun setAngleY(angle: Float) {
        directionY = sin(Math.toRadians(angle.toDouble())).toFloat()
    }

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("uEnable",mEnable)
        put("uSamples", samples)
        putVec2("uDirection", directionX, directionY)
        put("uStrength", strength)
    }

    override fun getFragmentShader(): String {
        return """
            precision mediump float;
            varying highp vec2 vTextureCoord;
            uniform sampler2D sTexture;
            uniform int uSamples;
            uniform vec2 uDirection;
            uniform float uStrength;
            uniform bool uEnable;

            void main() {
                if (uEnable) {
                    vec2 angle = uStrength * uDirection;
                    vec3 acc = vec3(0.0);
                    float delta = 2.0 / float(uSamples);
                    for (float i = -1.0; i <= 1.0; i += delta) {
                        acc += texture2D(sTexture, vTextureCoord - angle * i).rgb * delta * 0.5;
                    }
                    gl_FragColor = vec4(acc, 1.0);
                } else {
                    gl_FragColor = texture2D(sTexture, vTextureCoord);
                }
            }
        """.trimIndent()
    }
}