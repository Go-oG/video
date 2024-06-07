package com.goog.effect.model

import com.goog.effect.utils.checkArgs
import kotlin.reflect.KProperty

class FloatDelegate(
    defaultV: Float, minV: Float? = null, maxV: Float? = null,
    includeMin: Boolean = true, includeMax: Boolean = true) : NumberDelegate<Float>(defaultV, minV, maxV, includeMin, includeMax)

class IntDelegate(
    defaultV: Int, minV: Int? = null, maxV: Int? = null,
    includeMin: Boolean = true, includeMax: Boolean = true
) : NumberDelegate<Int>(defaultV, minV, maxV, includeMin, includeMax)

open class NumberDelegate<T : Number>(defaultV: T, val minV: T? = null, val maxV: T? = null,
    val includeMin: Boolean = true, val includeMax: Boolean = true) {
    private val accurate = 0.0000000001
    private var numberValue: T

    init {
        numberValue = checkValue(defaultV)
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return numberValue
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        numberValue = checkValue(value)
    }

    fun getCurrent(): T {
        return numberValue
    }

    private fun checkValue(nValue: T): T {
        val v1 = nValue.toDouble()
        if (minV != null) {
            val minv1 = minV.toDouble()
            val sub = v1 - minv1
            if (includeMin) {
                checkArgs(sub >= -accurate, "value($nValue) should be >= $minV")
            } else {
                checkArgs(sub > -accurate, "value($nValue) should be > $minV")
            }
        }
        if (maxV != null) {
            val maxv1 = maxV.toDouble()
            val sub = maxv1 - v1
            if (includeMax) {
                checkArgs(sub >= -accurate, "value($nValue) should be <= $maxV")
            } else {
                checkArgs(sub > accurate, "value($nValue) should be < $maxV")
            }
        }
        return nValue
    }


}