package com.goog.video

import org.junit.Test
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.sqrt

class ExampleUnitTest2 {

    @Test
    fun test1() {
        println("weights:${computeWeights(5, 3f, true, true).toList()}")
    }

    private fun computeWeights(blurSize: Int, sigma: Float, linear: Boolean, adjust: Boolean): FloatArray {
        val weights = mutableListOf<Float>()
        val sqrt2 = sqrt(2f)
        val powSigma = sigma * sigma

        val tt = sigma / sqrt2

        var sumWeights = 0.0f
        for (i in -blurSize..blurSize) {
            val w = if (adjust) {
                (erf((i + 0.5f) / tt) - erf((i - 0.5f) / tt)) / 2f
            } else {
                exp(-i * i / powSigma)
            }
            sumWeights += w
            weights.add(w)
        }

        for (index in weights.indices) {
            weights[index] /= sumWeights
        }
        val offsets = mutableListOf<Float>()
        var newWeights = mutableListOf<Float>()

        if (linear) {
            for (i in -blurSize..blurSize step 2) {
                if (i == blurSize) {
                    offsets.add(i.toFloat())
                    newWeights.add(weights[i + blurSize])
                } else {
                    val w0 = weights[i + blurSize + 0]
                    val w1 = weights[i + blurSize + 1]
                    val w = w0 + w1
                    if (w > 0) {
                        offsets.add(i + w1 / w);
                    } else {
                        offsets.add(i.toFloat())
                    }
                    newWeights.add(w)
                }
            }
        } else {
            for (i in -blurSize..blurSize) {
                offsets.add(i.toFloat())
            }
            newWeights = weights;
        }

        return newWeights.toFloatArray()
    }

    private fun erf(x: Float): Float {
        val a1 = 0.254829592
        val a2 = -0.284496736
        val a3 = 1.421413741
        val a4 = -1.453152027
        val a5 = 1.061405429
        val p = 0.3275911
        val sign = if (x >= 0) 1.0f else -1.0f
        val absX = abs(x)
        val t = 1.0f / (1.0f + p * absX)
        val poly = a1 * t + a2 * t * t + a3 * t * t * t + a4 * t * t * t * t + a5 * t * t * t * t * t
        return (sign * (1.0 - poly * exp(-1 * absX * absX))).toFloat()
    }
}