package com.goog.video.utils

import android.content.Context
import java.lang.Thread.currentThread

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
    return ContextUtil.getContext().assets.open(str).bufferedReader().use { it.readText() }
}

