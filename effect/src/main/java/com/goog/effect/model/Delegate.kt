package com.goog.effect.model

import kotlin.reflect.KProperty

class FloatDelegate(defaultV: Float, minV: Float? = null, maxV: Float? = null) : NumberDelegate<Float>(defaultV, minV, maxV)

class IntDelegate(
    defaultV: Int, minV: Int? = null, maxV: Int? = null) : NumberDelegate<Int>(defaultV, minV, maxV)

/**
 * 限定数据范围，对于超过范围的数值，我们将把其缩放到最大或最小值
 */
open class NumberDelegate<T : Number>(
    defaultV: T, val minV: T? = null, val maxV: T? = null) {
    private var numberValue: T

    init {
        numberValue = formatValue(defaultV)
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return numberValue
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        numberValue = formatValue(value)
    }

    fun getCurrent(): T {
        return numberValue
    }

    private fun formatValue(value: T): T {
        val v1 = value.toDouble()
        if (minV != null) {
            val minv1 = minV.toDouble()
            if (v1 < minv1) {
                return minV
            }
        }

        if (maxV != null) {
            val maxv1 = maxV.toDouble()
            if (v1 > maxv1) {
                return maxV
            }
        }
        return value
    }

}