package com.goog.videodemo.data

import com.goog.effect.filter.core.GLFilter
import kotlin.properties.Delegates

class Parameter(
    val showName: String,
    val methodName: String,
    val minValue: Float,
    val maxValue: Float,
    val step: Float,
    val useFloat: Boolean) {

    var curValue by Delegates.notNull<Float>()
}


class FilterItem(val showName: String, val clsName: Class<*>, val parameterList: List<Parameter>) {
    var filter: GLFilter? = null
    var select = false

    fun select() {
        select = true
        filter = clsName.newInstance() as GLFilter
    }

    fun unselect() {
        select = false
        filter = null
    }

    fun changeParameter(parameter: Parameter, value: Float) {
        val filter = this.filter ?: return
        val cls = filter::class.java
        val methodName = parameter.methodName
        val method = if (parameter.useFloat) cls.getMethod(methodName, Float::class.java) else cls.getMethod(methodName, Int::class.java)
        method.isAccessible = true
        if (parameter.useFloat) {
            method.invoke(filter, value)
        } else {
            method.invoke(filter, value.toInt())
        }
        parameter.curValue = value
    }

}

fun String.firstLowCase(): String {
    if (isBlank()) {
        return ""
    }
    return replaceFirstChar {
        if (it.isUpperCase()) it.lowercase() else it.toString()
    }
}










