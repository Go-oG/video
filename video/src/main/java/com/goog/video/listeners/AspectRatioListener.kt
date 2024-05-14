package com.goog.video.listeners

interface AspectRatioListener {
    fun onRatioUpdated(targetRatio: Float, naturalRatio: Float, ratioMismatch: Boolean)
}