package com.goog.video.utils

import android.graphics.Matrix

object ScaleHelper {
    const val FILL: Int = 0
    const val FIT_CENTER: Int = 1
    const val CROP_CENTER: Int = 2

    private fun getDiffX(viewWidth: Float, videoWidth: Float): Float {
        return if ((viewWidth > videoWidth)) viewWidth / videoWidth else videoWidth / viewWidth
    }

    private fun getDiffY(viewHeight: Float, videoHeight: Float): Float {
        return if ((viewHeight > videoHeight)) viewHeight / videoHeight else videoHeight / viewHeight
    }

    private fun getAspectRatio(width: Float, height: Float): Float {
        return width / height
    }

    private fun createScaleMatrix(scaleX: Float, scaleY: Float, width: Float, height: Float): Matrix {
        val matrix = Matrix()
        matrix.setScale(scaleX, scaleY, width / 2, height / 2)
        return matrix
    }

    fun getVideoScaleMatrix(viewWidth: Float, viewHeight: Float, videoWidth: Float, videoHeight: Float,
        scaleType: Int): Matrix? {
        val diffX = getDiffX(viewWidth, videoWidth)
        val diffY = getDiffY(viewHeight, videoHeight)
        val videoAspectRatio = getAspectRatio(videoWidth, videoHeight)
        if (scaleType == FIT_CENTER) {
            // Calculate the view scale with crop center
            val scaleX = getFitCenterX(viewWidth, viewHeight, videoWidth, videoHeight, diffX, diffY, videoAspectRatio)
            val scaleY = getFitCenterY(viewWidth, viewHeight, videoWidth, videoHeight, diffX, diffY, videoAspectRatio)
            return createScaleMatrix(scaleX, scaleY, viewWidth, viewHeight)
        } else if (scaleType == CROP_CENTER) {
            // Calculate the view scale with crop center
            val scaleX = getCropCenterX(viewWidth, viewHeight, videoWidth, videoHeight, diffX, diffY, videoAspectRatio)
            val scaleY = getCropCenterY(viewWidth, viewHeight, videoWidth, videoHeight, diffX, diffY, videoAspectRatio)
            return createScaleMatrix(scaleX, scaleY, viewWidth, viewHeight)
        } else if (scaleType == FILL) {
            return null
        }
        return null
    }

    private fun getFitCenterX(viewWidth: Float, viewHeight: Float, videoWidth: Float, videoHeight: Float, diffX: Float,
        diffY: Float, videoAspectRatio: Float): Float {
        val scaleX = if (viewWidth < videoWidth) {
            if (viewHeight < videoHeight) {
                if (diffX > diffY) {
                    1f
                } else {
                    viewHeight * videoAspectRatio / viewWidth
                }
            } else {
                1f
            }
        } else {
            if (viewHeight < videoHeight) {
                viewHeight * videoAspectRatio / viewWidth
            } else {
                if (diffX >= diffY) {
                    viewHeight * videoAspectRatio / viewWidth
                } else {
                    1f
                }
            }
        }
        return scaleX
    }

    private fun getFitCenterY(viewWidth: Float, viewHeight: Float, videoWidth: Float, videoHeight: Float, diffX: Float,
        diffY: Float, videoAspectRatio: Float): Float {
        val scaleY = if (viewHeight < videoHeight) {
            if (viewWidth < videoWidth) {
                if (diffY > diffX) {
                    1f
                } else {
                    viewWidth / videoAspectRatio / viewHeight
                }
            } else {
                1f
            }
        } else {
            if (viewWidth < videoWidth) {
                viewWidth / videoAspectRatio / viewHeight
            } else {
                if (diffY > diffX) {
                    viewWidth / videoAspectRatio / viewHeight
                } else {
                    1f
                }
            }
        }
        return scaleY
    }

    private fun getCropCenterX(viewWidth: Float, viewHeight: Float, videoWidth: Float, videoHeight: Float, diffX: Float,
        diffY: Float, videoAspectRatio: Float): Float {
        val scaleX = if (viewWidth < videoWidth) {
            if (viewHeight < videoHeight) {
                if (diffX > diffY) {
                    viewHeight * videoAspectRatio / viewWidth
                } else {
                    1f
                }
            } else {
                viewHeight * videoAspectRatio / viewWidth
            }
        } else {
            if (viewHeight < videoHeight) {
                1f
            } else {
                if (diffX >= diffY) {
                    1f
                } else {
                    viewHeight * videoAspectRatio / viewWidth
                }
            }
        }
        return scaleX
    }

    private fun getCropCenterY(viewWidth: Float, viewHeight: Float, videoWidth: Float, videoHeight: Float, diffX: Float,
        diffY: Float, videoAspectRatio: Float): Float {
        val scaleY = if (viewHeight < videoHeight) {
            if (viewWidth < videoWidth) {
                if (diffY > diffX) {
                    viewWidth / videoAspectRatio / viewHeight
                } else {
                    1f
                }
            } else {
                viewWidth / videoAspectRatio / viewHeight
            }
        } else {
            if (viewWidth < videoWidth) {
                1f
            } else {
                if (diffY > diffX) {
                    1f
                } else {
                    viewWidth / videoAspectRatio / viewHeight
                }
            }
        }
        return scaleY
    }
}
