package com.goog.video.utils

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