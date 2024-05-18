package com.goog.video.model

import com.goog.video.utils.checkArgs
import kotlin.reflect.KProperty

class FloatDelegate(
    defaultV: Float, val minV: Float? = null, val maxV: Float? = null,
    val includeMin: Boolean = true, val includeMax: Boolean = true) {
    private var floatValue = 0f

    init {
        if (minV != null && maxV != null) {
            checkArgs(minV <= maxV)
        }
        floatValue = defaultV
        if (minV != null) {
            if (includeMin) {
                checkArgs(defaultV >= minV)
            } else {
                checkArgs(defaultV > minV)
            }
        }
        if (maxV != null) {
            if (includeMax) {
                checkArgs(defaultV <= maxV)
            } else {
                checkArgs(defaultV < maxV)
            }
        }
        floatValue = defaultV
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): Float {
        return floatValue
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Float) {
        if (minV != null) {
            if (includeMin) {
                checkArgs(value >= minV)
            } else {
                checkArgs(value > minV)
            }
        }
        if (maxV != null) {
            if (includeMax) {
                checkArgs(value <= maxV)
            } else {
                checkArgs(value < maxV)
            }
        }

        floatValue = value
    }


    fun getCurrent():Float{
        return floatValue
    }

}

class IntDelegate(
    defaultV: Int, val minV: Int? = null, val maxV: Int? = null,
    val includeMin: Boolean = true, val includeMax: Boolean = true
) {
    private var intValue: Int = 0

    init {
        if (minV != null && maxV != null) {
            checkArgs(minV <= maxV)
        }
        if (minV != null) {
            if (includeMin) {
                checkArgs(defaultV >= minV)
            } else {
                checkArgs(defaultV > minV)
            }
        }
        if (maxV != null) {
            if (includeMax) {
                checkArgs(defaultV <= maxV)
            } else {
                checkArgs(defaultV < maxV)
            }
        }
        intValue = defaultV
    }


    operator fun getValue(thisRef: Any?, property: KProperty<*>): Int {
        return intValue
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
        if (minV != null) {
            if (includeMin) {
                checkArgs(value >= minV)
            } else {
                checkArgs(value > minV)
            }
        }
        if (maxV != null) {
            if (includeMax) {
                checkArgs(value <= maxV)
            } else {
                checkArgs(value < maxV)
            }
        }
        intValue = value
    }
    fun getCurrent():Int{
        return intValue
    }

}