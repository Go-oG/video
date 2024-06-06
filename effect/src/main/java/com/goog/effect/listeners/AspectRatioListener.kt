package com.goog.effect.listeners

interface AspectRatioListener {
    fun onRatioUpdated(targetRatio: Float, naturalRatio: Float, ratioMismatch: Boolean)
}