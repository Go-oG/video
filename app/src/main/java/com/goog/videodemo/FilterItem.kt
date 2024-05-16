package com.goog.videodemo

import android.content.Context
import android.graphics.BitmapFactory
import com.goog.video.filter.GLAlphaFrameFilter
import com.goog.video.filter.GLSuperResolutionFilter
import com.goog.video.filter.GLBilateralFilter
import com.goog.video.filter.GLBoxBlurFilter
import com.goog.video.filter.GLBrightnessFilter
import com.goog.video.filter.GLBulgeDistortionFilter
import com.goog.video.filter.GLCGAColorSpaceFilter
import com.goog.video.filter.GLContrastFilter
import com.goog.video.filter.GLCornerFilter
import com.goog.video.filter.GLCrosshatchFilter
import com.goog.video.filter.GLExposureFilter
import com.goog.video.filter.GLFilter
import com.goog.video.filter.GLGammaFilter
import com.goog.video.filter.GLGaussianBlur2Filter
import com.goog.video.filter.GLGaussianBlur3Filter
import com.goog.video.filter.GLGaussianBlurFilter
import com.goog.video.filter.GLGrayScaleFilter
import com.goog.video.filter.GLHalftoneFilter
import com.goog.video.filter.GLHazeFilter
import com.goog.video.filter.GLHighlightShadowFilter
import com.goog.video.filter.GLHueFilter
import com.goog.video.filter.GLInvertFilter
import com.goog.video.filter.GLLookUpTableFilter
import com.goog.video.filter.GLLuminanceFilter
import com.goog.video.filter.GLLuminanceThresholdFilter
import com.goog.video.filter.GLMonochromeFilter
import com.goog.video.filter.GLOpacityFilter
import com.goog.video.filter.GLPixelationFilter
import com.goog.video.filter.GLPosterizeFilter
import com.goog.video.filter.GLRGBFilter
import com.goog.video.filter.GLSaturationFilter
import com.goog.video.filter.GLSepiaFilter
import com.goog.video.filter.GLSharpenFilter
import com.goog.video.filter.GLSphereRefractionFilter
import com.goog.video.filter.GLSwirlFilter
import com.goog.video.filter.GLThreex3TextureSamplingFilter
import com.goog.video.filter.GLToneFilter
import com.goog.video.filter.GLVibranceFilter
import com.goog.video.filter.GLVignetteFilter
import com.goog.video.filter.GLWatermarkFilter
import com.goog.video.filter.GLWeakPixelInclusionFilter
import com.goog.video.filter.GLWhiteBalanceFilter
import com.goog.video.filter.GLZoomBlurFilter

class Parameter(val name: String, val index: Int, val minValue: Float, val maxValue: Float, val step: Float,
    var curValue: Float)

interface FilterBuilder {

    fun build(): GLFilter

    fun getParameters(): List<Parameter>

    fun changeParameter(filter: GLFilter, index: Int, value: Float)

}

class FilterItem(val name: String, val builder: FilterBuilder) {

    var filter: GLFilter? = null

    var select = false

    fun select() {
        select = true
        filter = builder.build()
    }

    fun unselect() {
        select = false
        filter = null
    }

    companion object {
        fun loadFiltersData(context: Context): List<FilterItem> {
            val list = mutableListOf<FilterItem>()
            list.add(alphaFrameFilter())

            list.add(bilateralFilter())
            list.add(boxBlurFilter())
            list.add(brightnessFilter())
            list.add(bulgeDistortionFilter())

            list.add(cgaColorSpaceFilter())
            list.add(contrastFilter())
            list.add(cornerFilter())
            list.add(crosshatchFilter())

            list.add(exposureFilter())

            list.add(gammaFilter())
            list.add(gaussianBlurFilter())
            list.add(gaussianBlur2Filter())
            list.add(gaussianBlur3Filter())
            list.add(grayScaleFilter())

            list.add(halfToneFilter())
            list.add(hazeFilter())
            list.add(highlightShadowFilter())
            list.add(hueFilter())

            list.add(invertFilter())

            list.add(lookUpTableFilter(context))
            list.add(luminanceFilter())
            list.add(luminanceThresholdFilter())

            list.add(monochromeFilter())

            list.add(opacityFilter())

            list.add(pixelationFilter())
            list.add(posterizeFilter())

            list.add(rgbFilter())

            list.add(saturationFilter())
            list.add(sepiaFilter())
            list.add(sharpenFilter())
            list.add(sphereRefractionFilter())
            list.add(superResolutionFilter())
            list.add(swirlFilter())

            list.add(threeX3SamplingFilter())
            list.add(toneFilter())

            list.add(vibranceFilter())
            list.add(vignetterFilter())

            list.add(waterMarkFilter(context))
            list.add(weakPixelInclusionFilter())
            list.add(whiteBalanceFilter())

            list.add(zoomBlurFilter())

            return list
        }

        private fun alphaFrameFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GLFilter {
                    return GLAlphaFrameFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf()
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                }
            }
            return FilterItem("AlphaFrame", builder)
        }

        private fun bilateralFilter(): FilterItem {
            val builder1 = object : FilterBuilder {
                override fun build(): GLFilter {
                    return GLBilateralFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("texelWidthOffset", 0, 0f, 1f, 0.001f, 0.004f),
                            Parameter("texelHeightOffset", 1, 0f, 1f, 0.001f, 0.004f),
                            Parameter("blurSize", 2, 1f, 100f, 1f, 1f),
                    )
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                    val bf = filter as GLBilateralFilter
                    when (index) {
                        0 -> bf.texelWidthOffset = value
                        1 -> bf.texelHeightOffset = value
                        2 -> bf.blurSize = value
                    }
                }
            }
            return FilterItem("Bilateral", builder1)
        }

        private fun boxBlurFilter(): FilterItem {
            val builder2 = object : FilterBuilder {
                override fun build(): GLFilter {
                    return GLBoxBlurFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("texelWidthOffset", 0, 0f, 1f, 0.001f, 0.003f),
                            Parameter("texelHeightOffset", 1, 0f, 1f, 0.001f, 0.003f),
                            Parameter("blurSize", 2, 1f, 100f, 1f, 1f),
                    )
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                    val bf = filter as GLBoxBlurFilter
                    when (index) {
                        0 -> bf.texelWidthOffset = value
                        1 -> bf.texelHeightOffset = value
                        2 -> bf.blurSize = value
                    }
                }
            }

            return FilterItem("BoxBlur", builder2)

        }

        private fun brightnessFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GLFilter {
                    return GLBrightnessFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(Parameter("brightness", 0, -1f, 1f, 0.01f, 0f))
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                    val bf = filter as GLBrightnessFilter
                    bf.setBrightness(value)
                }
            }

            return FilterItem("Brightness", builder)

        }

        private fun bulgeDistortionFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GLFilter {
                    return GLBulgeDistortionFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("centerX", 0, 0f, 1f, 0.01f, 0.5f),
                            Parameter("centerY", 1, 0f, 1f, 0.01f, 0.5f),
                            Parameter("radius", 2, 0.001f, 5f, 0.01f, 0.25f),
                            Parameter("scale", 3, 0f, 10f, 0.01f, 0.5f),
                    )
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                    val bf = filter as GLBulgeDistortionFilter
                    when (index) {
                        0 -> bf.setCenterX(value)
                        1 -> bf.setCenterY(value)
                        2 -> bf.setRadius(value)
                        3 -> bf.setScale(value)
                    }
                }
            }

            return FilterItem("BulgeDistortion", builder)

        }

        //=========================================================
        private fun cgaColorSpaceFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GLFilter {
                    return GLCGAColorSpaceFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf()
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                }
            }

            return FilterItem("CGAColorSpace", builder)

        }

        private fun contrastFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GLFilter {
                    return GLContrastFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("contrast", 0, 0f, 4f, 0.01f, 1.2f),
                    )
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                    val bf = filter as GLContrastFilter
                    bf.setContrast(value)
                }
            }

            return FilterItem("Contrast", builder)

        }

        private fun cornerFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GLFilter {
                    return GLCornerFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("LT", 0, 0f, 30f, 1f, 0f),
                            Parameter("RT", 1, 0f, 30f, 1f, 0f),
                            Parameter("LB", 2, 0f, 30f, 1f, 0f),
                            Parameter("RB", 3, 0f, 30f, 1f, 0f),
                    )
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                    val bf = filter as GLCornerFilter
                    when (index) {
                        0 -> bf.setTopLeftCorner(value)
                        1 -> bf.setTopRightCorner(value)
                        2 -> bf.setBottomLeftCorner(value)
                        3 -> bf.setBottomRightCorner(value)
                    }
                }
            }

            return FilterItem("Corner", builder)

        }

        private fun crosshatchFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GLFilter {
                    return GLCrosshatchFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("crossHatchSpacing", 0, 0f, 3f, 0.01f, 0.03f),
                            Parameter("lineWidth", 1, 0.001f, 2f, 0.001f, 0.003f)
                    )
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                    val bf = filter as GLCrosshatchFilter
                    when (index) {
                        0 -> bf.setCrossHatchSpacing(value)
                        1 -> bf.setLineWidth(value)
                    }
                }
            }

            return FilterItem("Crosshatch", builder)

        }

        //=========================================================
        private fun exposureFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GLFilter {
                    return GLExposureFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("exposure", 0, -10f, 10f, 0.1f, 1f),
                    )
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                    val bf = filter as GLExposureFilter
                    bf.setExposure(value)
                }
            }

            return FilterItem("Exposure", builder)

        }

        //=========================================================
        private fun gammaFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GLFilter {
                    return GLGammaFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(Parameter("gamma", 0, 0f, 20f, 0.01f, 1.2f))
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                    val bf = filter as GLGammaFilter
                    bf.gamma = value
                }
            }
            return FilterItem("Gamma", builder)
        }

        private fun gaussianBlurFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GLFilter {
                    return GLGaussianBlurFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("texelWidthOffset", 0, 0f, 1f, 0.001f, 0.01f),
                            Parameter("texelHeightOffset", 1, 0f, 1f, 0.001f, 0.01f),
                            Parameter("blurSize", 2, 0.1f, 3f, 0.1f, 0.2f),
                    )
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                    val bf = filter as GLGaussianBlurFilter
                    when (index) {
                        0 -> bf.setTexelWidthOffset(value)
                        1 -> bf.setTexelWidthOffset(value)
                        2 -> bf.setBlurSize(value)
                    }
                }
            }
            return FilterItem("GaussianBlur", builder)
        }

        private fun gaussianBlur2Filter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GLFilter {
                    return GLGaussianBlur2Filter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("samples", 0, 1f, 50f, 1f, 25f),
                            Parameter("scale", 1, 1f, 30f, 1f, 8f),
                    )
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                    val bf = filter as GLGaussianBlur2Filter
                    when (index) {
                        0 -> bf.setSamplesSize(value.toInt())
                        1 -> bf.setScaleFactor(value.toInt())
                    }
                }
            }
            return FilterItem("GaussianBlur2", builder)
        }

        private fun gaussianBlur3Filter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GLFilter {
                    return GLGaussianBlur3Filter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("blurSize", 0, 2f, 60f, 1f, 3f)
                    )
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                    val bf = filter as GLGaussianBlur3Filter
                    bf.setBlurSize(value.toInt())
                }
            }
            return FilterItem("GaussianBlur3", builder)
        }

        private fun grayScaleFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GLFilter {
                    return GLGrayScaleFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf()
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                }
            }
            return FilterItem("GrayScale", builder)
        }

        //========================================================

        private fun halfToneFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GLFilter {
                    return GLHalftoneFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("fractionalWidthOfPixel", 0, 0.01f, 1f, 0.01f, 0.01f),
                            Parameter("aspectRatio", 1, 0.1f, 10f, 0.1f, 1f),
                    )
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                    val bf = filter as GLHalftoneFilter
                    when (index) {
                        0 -> bf.setFractionalWidthOfAPixel(value)
                        1 -> bf.setAspectRatio(value)
                    }
                }
            }
            return FilterItem("HalfTone", builder)
        }

        private fun hazeFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GLFilter {
                    return GLHazeFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("distance", 0, 0f, 1f, 0.01f, 0.2f),
                            Parameter("slope", 1, -1f, 1f, 0.01f, 0f),
                    )
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                    val bf = filter as GLHazeFilter
                    when (index) {
                        0 -> bf.setDistance(value)
                        1 -> bf.setSlope(value)
                    }
                }
            }
            return FilterItem("Haze", builder)
        }

        private fun highlightShadowFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GLFilter {
                    return GLHighlightShadowFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("shadows", 0, 1f, 50f, 0.1f, 1f),
                            Parameter("highlights", 1, 0f, 1f, 0.01f, 0f),
                    )
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                    val bf = filter as GLHighlightShadowFilter
                    when (index) {
                        0 -> bf.setShadows(value)
                        1 -> bf.setHighlights(value)
                    }
                }
            }
            return FilterItem("HighlightShadow", builder)
        }

        private fun hueFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GLFilter {
                    return GLHueFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("hue", 0, 0f, 360f, 1f, 90f))
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                    val bf = filter as GLHueFilter
                    bf.setHue(value)
                }
            }
            return FilterItem("Hue", builder)
        }

        //========================================================

        private fun invertFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GLFilter {
                    return GLInvertFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf()
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                }
            }
            return FilterItem("Invert", builder)
        }

        private fun lookUpTableFilter(context: Context): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GLFilter {
                    val bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher_round)
                    return GLLookUpTableFilter(bitmap)
                }

                override fun getParameters(): List<Parameter> {
                    return listOf()
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                }
            }
            return FilterItem("LookUpTable", builder)
        }

        private fun luminanceFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GLFilter {
                    return GLLuminanceFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf()
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                }
            }
            return FilterItem("Luminance", builder)
        }

        private fun luminanceThresholdFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GLFilter {
                    return GLLuminanceThresholdFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("threshold", 0, 0f, 1f, 0.01f, 0.5f),
                    )
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                    val bf = filter as GLLuminanceThresholdFilter
                    bf.setThreshold(value)
                }
            }
            return FilterItem("LuminanceThreshold", builder)
        }

        private fun monochromeFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GLFilter {
                    return GLMonochromeFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("intensity", 0, 0f, 1f, 0.01f, 1f),
                    )
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                    val bf = filter as GLMonochromeFilter
                    bf.setIntensity(value)
                }
            }
            return FilterItem("Monochrome", builder)
        }

        private fun opacityFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GLFilter {
                    return GLOpacityFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("opacity", 0, 0f, 1f, 0.01f, 1f),
                    )
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                    val bf = filter as GLOpacityFilter
                    bf.opacity = (value)
                }
            }
            return FilterItem("Opacity", builder)
        }

        private fun pixelationFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GLFilter {
                    return GLPixelationFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("pixel", 0, 1f, 100f, 1f, 1f),
                            Parameter("imageWidthFactor", 1, 0.001f, 1f, 0.001f, 0.0014f),
                            Parameter("imageHeightFactor", 2, 0.001f, 1f, 0.001f, 0.0014f),
                    )
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                    val bf = filter as GLPixelationFilter
                    bf.setPixel(value)
                    when (index) {
                        0 -> bf.setPixel(value)
                        1 -> bf.setImageWidthFactor(value)
                        2 -> bf.setImageHeightFactor(value)
                    }
                }
            }
            return FilterItem("Pixelation", builder)
        }

        private fun posterizeFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GLFilter {
                    return GLPosterizeFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("colorLevels", 0, 1f, 256f, 1f, 10f),
                    )
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                    val bf = filter as GLPosterizeFilter
                    bf.setColorLevels(value.toInt())
                }
            }
            return FilterItem("Posterize", builder)
        }

        private fun rgbFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GLFilter {
                    return GLRGBFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("red", 0, 0f, 1f, 0.01f, 1f),
                            Parameter("green", 1, 0f, 1f, 0.01f, 1f),
                            Parameter("blue", 2, 0f, 1f, 0.01f, 1f),
                            Parameter("brightness", 3, 0f, 1f, 0.01f, 0f),
                            Parameter("saturation", 4, 0f, 1f, 0.01f, 1f),
                            Parameter("contrast", 5, 0f, 10f, 0.01f, 1.2f),
                    )
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                    val bf = filter as GLRGBFilter
                    when (index) {
                        0 -> bf.setRed(value)
                        1 -> bf.setGreen(value)
                        2 -> bf.setBlue(value)
                        3 -> bf.setBrightness(value)
                        4 -> bf.setSaturation(value)
                        5 -> bf.setContrast(value)
                    }
                }
            }
            return FilterItem("RGB", builder)
        }

        private fun saturationFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GLFilter {
                    return GLSaturationFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("saturation", 0, 0f, 2f, 0.01f, 1f))
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                    val bf = filter as GLSaturationFilter
                    bf.setSaturation(value)
                }
            }
            return FilterItem("Saturation", builder)
        }

        private fun sepiaFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GLFilter {
                    return GLSepiaFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf()
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                }
            }
            return FilterItem("Sepia", builder)
        }

        private fun sharpenFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GLFilter {
                    return GLSharpenFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("sharpness", 0, 0f, 1f, 0.01f, 1f),
                    )
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                    val bf = filter as GLSharpenFilter
                    bf.setsSharpness(value)
                }
            }
            return FilterItem("Sharpen", builder)
        }

        private fun sphereRefractionFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GLFilter {
                    return GLSphereRefractionFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("centerX", 0, 0f, 1f, 0.01f, 0.5f),
                            Parameter("centerY", 1, 0f, 1f, 0.01f, 0.5f),
                            Parameter("radius", 2, 0f, 1f, 0.01f, 0.5f),
                            Parameter("aspectRatio", 3, 0.1f, 10f, 0.1f, 0.5f),
                            Parameter("refractiveIndex", 4, 0f, 1f, 0.01f, 0.71f),
                    )
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                    val bf = filter as GLSphereRefractionFilter
                    when (index) {
                        0 -> bf.setCenterX(value)
                        1 -> bf.setCenterY(value)
                        2 -> bf.setRadius(value)
                        3 -> bf.setAspectRatio(value)
                        4 -> bf.setRefractiveIndex(value)
                    }
                }
            }
            return FilterItem("SphereRefraction", builder)
        }

        private fun superResolutionFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GLFilter {
                    return GLSuperResolutionFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("edgeThreshold", 0, 0.01f, 1f, 0.01f, 0.031372f),
                            Parameter("edgeSharpness", 1, 0.1f, 10f, 0.1f, 2f)
                    )
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                    val bf = filter as GLSuperResolutionFilter
                    when (index) {
                        0 -> bf.setEdgeThreshold(value)
                        1 -> bf.setEdgeSharpness(value)
                    }
                }
            }
            return FilterItem("SuperResolution", builder)
        }

        private fun swirlFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GLFilter {
                    return GLSwirlFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("centerX", 0, 0f, 1f, 0.01f, 0.5f),
                            Parameter("centerY", 1, 0f, 1f, 0.01f, 0.5f),
                            Parameter("radius", 2, 0f, 1f, 0.01f, 0.5f),
                            Parameter("angle", 3, 0f, 2f, 0.01f, 1f),
                    )
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                    val bf = filter as GLSwirlFilter
                    when (index) {
                        0 -> bf.setCenterX(value)
                        1 -> bf.setCenterY(value)
                        2 -> bf.setRadius(value)
                        3 -> bf.setAngle(value)
                    }
                }
            }
            return FilterItem("Swirl", builder)
        }

        private fun threeX3SamplingFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GLFilter {
                    return GLThreex3TextureSamplingFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf()
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                }
            }
            return FilterItem("ThreeX3Sampling", builder)
        }

        private fun toneFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GLFilter {
                    return GLToneFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("threshold", 0, 0f, 2f, 0.01f, 0.2f),
                            Parameter("quantizationLevels", 1, 1f, 100f, 1f, 10f),
                    )
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                    val bf = filter as GLToneFilter
                    when (index) {
                        0 -> bf.setThreshold(value)
                        1 -> bf.setQuantizationLevels(value)
                    }
                }
            }
            return FilterItem("Tone", builder)
        }

        private fun vibranceFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GLFilter {
                    return GLVibranceFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("vibrance", 0, 0f, 2f, 0.01f, 0f),
                    )
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                    val bf = filter as GLVibranceFilter
                    bf.setVibrance(value)
                }
            }
            return FilterItem("Vibrance", builder)
        }

        private fun vignetterFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GLFilter {
                    return GLVignetteFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("centerX", 0, 0f, 1f, 0.01f, 0.5f),
                            Parameter("centerY", 1, 0f, 1f, 0.01f, 0.5f),
                            Parameter("vignetteStart", 2, 0f, 1f, 0.01f, 0.2f),
                            Parameter("vignetteEnd", 3, 0f, 1f, 0.01f, 0.85f))
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                    val bf = filter as GLVignetteFilter
                    when (index) {
                        0 -> bf.setCenterX(value)
                        1 -> bf.setCenterY(value)
                        2 -> bf.setVignetteStart(value)
                        3 -> bf.setVignetteEnd(value)
                    }
                }
            }
            return FilterItem("Vignette", builder)
        }

        private fun waterMarkFilter(context: Context): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GLFilter {
                    val bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher_round)
                    return GLWatermarkFilter(bitmap)
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("position", 0, 0f, 3f, 1f, 0f),
                    )
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                    val bf = filter as GLWatermarkFilter
                    when (value.toInt()) {
                        0 -> bf.position = GLWatermarkFilter.Position.LEFT_TOP
                        1 -> bf.position = GLWatermarkFilter.Position.RIGHT_TOP
                        2 -> bf.position = GLWatermarkFilter.Position.LEFT_BOTTOM
                        3 -> bf.position = GLWatermarkFilter.Position.RIGHT_BOTTOM
                    }
                }
            }
            return FilterItem("WaterMark", builder)
        }

        private fun weakPixelInclusionFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GLFilter {
                    return GLWeakPixelInclusionFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf()
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {}
            }
            return FilterItem("WeakPixelInclusion", builder)
        }

        private fun whiteBalanceFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GLFilter {
                    return GLWhiteBalanceFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("temperature", 0, 1000f, 20000f, 100f, 5000f),
                            Parameter("tint", 1, -100f, 100f, 1f, 0f))
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                    val bf = filter as GLWhiteBalanceFilter
                    when (index) {
                        0 -> bf.setTemperature(value)
                        1 -> bf.setTint(value)
                    }
                }
            }
            return FilterItem("WhiteBalance", builder)
        }

        private fun zoomBlurFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GLFilter {
                    return GLZoomBlurFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("centerX", 0, 0f, 1f, 0.01f, 0.5f),
                            Parameter("centerY", 1, 0f, 1f, 0.01f, 0.5f),
                            Parameter("blurSize", 2, 1f, 25f, 1f, 1f))
                }

                override fun changeParameter(filter: GLFilter, index: Int, value: Float) {
                    val bf = filter as GLZoomBlurFilter
                    when (index) {
                        0 -> bf.setCenterX(value)
                        1 -> bf.setCenterY(value)
                        2 -> bf.setBlurSize(value)
                    }
                }
            }
            return FilterItem("ZoomBlur", builder)
        }
    }

}