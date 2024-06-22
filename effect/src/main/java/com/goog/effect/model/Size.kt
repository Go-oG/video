package com.goog.effect.model

class Size(val width: Int, val height: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return other is Size && other.width == width && other.height == height
    }

    override fun hashCode(): Int {
        var result = width
        result = 31 * result + height
        return result
    }
}