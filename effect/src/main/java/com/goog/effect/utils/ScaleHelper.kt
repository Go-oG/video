package com.goog.effect.utils

import android.graphics.Matrix
import com.goog.effect.model.Size
import com.goog.effect.utils.ScaleHelper.PivotPoint
import com.goog.effect.utils.ScaleHelper.ScalableType
import java.lang.IllegalArgumentException
import kotlin.math.max
import kotlin.math.min

class ScaleHelper(val viewSize: Size, val videoSize: Size) {

    fun getScaleMatrix(scalableType: ScalableType): Matrix? {
        when (scalableType) {
            ScalableType.NONE -> return getNoScale()

            ScalableType.FIT_XY -> return fitXY()
            ScalableType.FIT_CENTER -> return fitCenter()
            ScalableType.FIT_START -> return fitStart()
            ScalableType.FIT_END -> return fitEnd()

            ScalableType.LEFT_TOP -> return getOriginalScale(PivotPoint.LEFT_TOP)
            ScalableType.LEFT_CENTER -> return getOriginalScale(PivotPoint.LEFT_CENTER)
            ScalableType.LEFT_BOTTOM -> return getOriginalScale(PivotPoint.LEFT_BOTTOM)
            ScalableType.CENTER_TOP -> return getOriginalScale(PivotPoint.CENTER_TOP)
            ScalableType.CENTER -> return getOriginalScale(PivotPoint.CENTER)
            ScalableType.CENTER_BOTTOM -> return getOriginalScale(PivotPoint.CENTER_BOTTOM)
            ScalableType.RIGHT_TOP -> return getOriginalScale(PivotPoint.RIGHT_TOP)
            ScalableType.RIGHT_CENTER -> return getOriginalScale(PivotPoint.RIGHT_CENTER)
            ScalableType.RIGHT_BOTTOM -> return getOriginalScale(PivotPoint.RIGHT_BOTTOM)

            ScalableType.LEFT_TOP_CROP -> return getCropScale(PivotPoint.LEFT_TOP)
            ScalableType.LEFT_CENTER_CROP -> return getCropScale(PivotPoint.LEFT_CENTER)
            ScalableType.LEFT_BOTTOM_CROP -> return getCropScale(PivotPoint.LEFT_BOTTOM)
            ScalableType.CENTER_TOP_CROP -> return getCropScale(PivotPoint.CENTER_TOP)
            ScalableType.CENTER_CROP -> return getCropScale(PivotPoint.CENTER)
            ScalableType.CENTER_BOTTOM_CROP -> return getCropScale(PivotPoint.CENTER_BOTTOM)
            ScalableType.RIGHT_TOP_CROP -> return getCropScale(PivotPoint.RIGHT_TOP)
            ScalableType.RIGHT_CENTER_CROP -> return getCropScale(PivotPoint.RIGHT_CENTER)
            ScalableType.RIGHT_BOTTOM_CROP -> return getCropScale(PivotPoint.RIGHT_BOTTOM)

            ScalableType.START_INSIDE -> return startInside()
            ScalableType.CENTER_INSIDE -> return centerInside()
            ScalableType.END_INSIDE -> return endInside()

            else -> return null
        }
    }

    private fun getMatrix(sx: Float, sy: Float, px: Float, py: Float): Matrix {
        val matrix = Matrix()
        matrix.setScale(sx, sy, px, py)
        return matrix
    }

    private fun getMatrix(sx: Float, sy: Float, pivotPoint: PivotPoint): Matrix {
        return when (pivotPoint) {
            PivotPoint.LEFT_TOP -> getMatrix(sx, sy, 0f, 0f)
            PivotPoint.LEFT_CENTER -> getMatrix(sx, sy, 0f, viewSize.height / 2f)
            PivotPoint.LEFT_BOTTOM -> getMatrix(sx, sy, 0f, viewSize.height.toFloat())
            PivotPoint.CENTER_TOP -> getMatrix(sx, sy, viewSize.width / 2f, 0f)
            PivotPoint.CENTER -> getMatrix(sx, sy, viewSize.width / 2f, viewSize.height / 2f)
            PivotPoint.CENTER_BOTTOM -> getMatrix(sx, sy, viewSize.width / 2f, viewSize.height.toFloat())
            PivotPoint.RIGHT_TOP -> getMatrix(sx, sy, viewSize.width.toFloat(), 0f)
            PivotPoint.RIGHT_CENTER -> getMatrix(sx, sy, viewSize.width.toFloat(), viewSize.height / 2f)
            PivotPoint.RIGHT_BOTTOM -> getMatrix(sx, sy, viewSize.width.toFloat(), viewSize.height.toFloat())
            else -> throw IllegalArgumentException("Illegal PivotPoint")
        }
    }

    private fun getNoScale(): Matrix {
        val sx = videoSize.width / viewSize.width.toFloat()
        val sy = videoSize.height / viewSize.height.toFloat()
        return getMatrix(sx, sy, PivotPoint.LEFT_TOP)
    }

    private fun getFitScale(pivotPoint: PivotPoint): Matrix {
        var sx = viewSize.width.toFloat() / videoSize.width
        var sy = viewSize.height.toFloat() / videoSize.height
        val minScale: Float = min(sx.toDouble(), sy.toDouble()).toFloat()
        sx = minScale / sx
        sy = minScale / sy
        return getMatrix(sx, sy, pivotPoint)
    }

    private fun fitXY(): Matrix {
        return getMatrix(1f, 1f, PivotPoint.LEFT_TOP)
    }

    private fun fitStart(): Matrix {
        return getFitScale(PivotPoint.LEFT_TOP)
    }

    private fun fitCenter(): Matrix {
        return getFitScale(PivotPoint.CENTER)
    }

    private fun fitEnd(): Matrix {
        return getFitScale(PivotPoint.RIGHT_BOTTOM)
    }

    private fun getOriginalScale(pivotPoint: PivotPoint): Matrix {
        val sx = videoSize.width / viewSize.width.toFloat()
        val sy = videoSize.height / viewSize.height.toFloat()
        return getMatrix(sx, sy, pivotPoint)
    }

    private fun getCropScale(pivotPoint: PivotPoint): Matrix {
        var sx = viewSize.width.toFloat() / videoSize.width
        var sy = viewSize.height.toFloat() / videoSize.height
        val maxScale: Float = max(sx.toDouble(), sy.toDouble()).toFloat()
        sx = maxScale / sx
        sy = maxScale / sy
        return getMatrix(sx, sy, pivotPoint)
    }

    private fun startInside(): Matrix {
        return if (videoSize.height <= viewSize.width && videoSize.height <= viewSize.height) {
            getOriginalScale(PivotPoint.LEFT_TOP)
        } else {
            fitStart()
        }
    }

    private fun centerInside(): Matrix {
        return if (videoSize.height <= viewSize.width
            && videoSize.height <= viewSize.height
        ) {
            getOriginalScale(PivotPoint.CENTER)
        } else {
            fitCenter()
        }
    }

    private fun endInside(): Matrix {
        return if (videoSize.height <= viewSize.width && videoSize.height <= viewSize.height) {
            getOriginalScale(PivotPoint.RIGHT_BOTTOM)
        } else {
            fitEnd()
        }
    }

    enum class ScalableType {
        NONE,

        FIT_XY,
        FIT_START,
        FIT_CENTER,
        FIT_END,

        LEFT_TOP,
        LEFT_CENTER,
        LEFT_BOTTOM,
        CENTER_TOP,
        CENTER,
        CENTER_BOTTOM,
        RIGHT_TOP,
        RIGHT_CENTER,
        RIGHT_BOTTOM,

        LEFT_TOP_CROP,
        LEFT_CENTER_CROP,
        LEFT_BOTTOM_CROP,
        CENTER_TOP_CROP,
        CENTER_CROP,
        CENTER_BOTTOM_CROP,
        RIGHT_TOP_CROP,
        RIGHT_CENTER_CROP,
        RIGHT_BOTTOM_CROP,

        START_INSIDE,
        CENTER_INSIDE,
        END_INSIDE
    }

    enum class PivotPoint {
        LEFT_TOP,
        LEFT_CENTER,
        LEFT_BOTTOM,
        CENTER_TOP,
        CENTER,
        CENTER_BOTTOM,
        RIGHT_TOP,
        RIGHT_CENTER,
        RIGHT_BOTTOM
    }

}