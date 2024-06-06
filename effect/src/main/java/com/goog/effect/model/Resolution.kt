package com.goog.effect.model

class Resolution(val width: Int, val height: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return other is Resolution && other.width == width && other.height == height
    }

    override fun hashCode(): Int {
        var result = width
        result = 31 * result + height
        return result
    }
}
