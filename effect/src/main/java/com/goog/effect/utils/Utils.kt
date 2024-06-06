package com.goog.effect.utils

import android.content.res.Resources
import java.lang.Thread.currentThread
import kotlin.math.floor

fun safeRun(block: () -> Unit) {
    try {
        block.invoke()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun safeInterrupt(block: () -> Unit) {
    try {
        block.invoke()
    } catch (e: InterruptedException) {
        currentThread().interrupt()
    }
}

fun checkArgs(value: Boolean, error: String = "Args check failed") {
    if (!value) {
        throw IllegalArgumentException(error)
    }
}

fun loadFilterFromAsset(assetName: String): String {
    var str = assetName
    if (!str.startsWith("filters")) {
        str = if (str.startsWith("/")) {
            "filters$str"
        } else {
            "filters/$str"
        }
    }
    val s = ContextUtil.getContext().assets.open(str).bufferedReader().use { it.readText() }

    if (s.isBlank()) {
        throw IllegalArgumentException()
    }
    return s
}

val DENSITY = Resources.getSystem().displayMetrics.density

val Number.dp: Float
    get() {
        return floor((this.toFloat() * DENSITY + 0.5f))
    }

val Any.TAG: String
    get() {
        return this::class.java.simpleName
    }
