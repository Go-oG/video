package com.goog.videodemo

import android.content.Context
import android.graphics.BitmapFactory
import com.goog.video.filter.GlBilateralFilter
import com.goog.video.filter.GlBoxBlurFilter
import com.goog.video.filter.GlBrightnessFilter
import com.goog.video.filter.GlBulgeDistortionFilter
import com.goog.video.filter.GlCGAColorSpaceFilter
import com.goog.video.filter.GlContrastFilter
import com.goog.video.filter.GlCornerFilter
import com.goog.video.filter.GlCrosshatchFilter
import com.goog.video.filter.GlExposureFilter
import com.goog.video.filter.GlFilter
import com.goog.video.filter.GlGammaFilter
import com.goog.video.filter.GlGaussianBlur2Filter
import com.goog.video.filter.GlGaussianBlur3Filter
import com.goog.video.filter.GlGaussianBlurFilter
import com.goog.video.filter.GlGrayScaleFilter
import com.goog.video.filter.GlHalftoneFilter
import com.goog.video.filter.GlHazeFilter
import com.goog.video.filter.GlHighlightShadowFilter
import com.goog.video.filter.GlHueFilter
import com.goog.video.filter.GlInvertFilter
import com.goog.video.filter.GlLookUpTableFilter
import com.goog.video.filter.GlLuminanceFilter
import com.goog.video.filter.GlLuminanceThresholdFilter
import com.goog.video.filter.GlMonochromeFilter
import com.goog.video.filter.GlOpacityFilter
import com.goog.video.filter.GlPixelationFilter
import com.goog.video.filter.GlPosterizeFilter
import com.goog.video.filter.GlRGBFilter
import com.goog.video.filter.GlSaturationFilter
import com.goog.video.filter.GlSepiaFilter
import com.goog.video.filter.GlSharpenFilter
import com.goog.video.filter.GlSolarizeFilter
import com.goog.video.filter.GlSphereRefractionFilter
import com.goog.video.filter.GlSwirlFilter
import com.goog.video.filter.GlThreex3TextureSamplingFilter
import com.goog.video.filter.GlToneCurveFilter
import com.goog.video.filter.GlToneFilter
import com.goog.video.filter.GlVibranceFilter
import com.goog.video.filter.GlVignetteFilter
import com.goog.video.filter.GlWatermarkFilter
import com.goog.video.filter.GlWeakPixelInclusionFilter
import com.goog.video.filter.GlWhiteBalanceFilter
import com.goog.video.filter.GlZoomBlurFilter

class Parameter(val name: String, val index: Int, val minValue: Float, val maxValue: Float, val step: Float,
    var curValue: Float)

interface FilterBuilder {

    fun build(): GlFilter

    fun getParameters(): List<Parameter>

    fun changeParameter(filter: GlFilter, index: Int, value: Float)

}

class FilterItem(val name: String, val builder: FilterBuilder) {

    var filter: GlFilter? = null

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

        private fun bilateralFilter(): FilterItem {
            val builder1 = object : FilterBuilder {
                override fun build(): GlFilter {
                    return GlBilateralFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("texelWidthOffset", 0, 0f, 1f, 0.001f, 0.004f),
                            Parameter("texelHeightOffset", 1, 0f, 1f, 0.001f, 0.004f),
                            Parameter("blurSize", 2, 1f, 100f, 1f, 1f),
                    )
                }

                override fun changeParameter(filter: GlFilter, index: Int, value: Float) {
                    val bf = filter as GlBilateralFilter
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
                override fun build(): GlFilter {
                    return GlBoxBlurFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("texelWidthOffset", 0, 0f, 1f, 0.001f, 0.003f),
                            Parameter("texelHeightOffset", 1, 0f, 1f, 0.001f, 0.003f),
                            Parameter("blurSize", 2, 1f, 100f, 1f, 1f),
                    )
                }

                override fun changeParameter(filter: GlFilter, index: Int, value: Float) {
                    val bf = filter as GlBoxBlurFilter
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
                override fun build(): GlFilter {
                    return GlBrightnessFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(Parameter("brightness", 0, -1f, 1f, 0.01f, 0f))
                }

                override fun changeParameter(filter: GlFilter, index: Int, value: Float) {
                    val bf = filter as GlBrightnessFilter
                    bf.setBrightness(value)
                }
            }

            return FilterItem("Brightness", builder)

        }

        private fun bulgeDistortionFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GlFilter {
                    return GlBulgeDistortionFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("centerX", 0, 0f, 1f, 0.01f, 0.5f),
                            Parameter("centerY", 1, 0f, 1f, 0.01f, 0.5f),
                            Parameter("radius", 2, 0.001f, 5f, 0.01f, 0.25f),
                            Parameter("scale", 3, 0f, 10f, 0.01f, 0.5f),
                    )
                }

                override fun changeParameter(filter: GlFilter, index: Int, value: Float) {
                    val bf = filter as GlBulgeDistortionFilter
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
                override fun build(): GlFilter {
                    return GlCGAColorSpaceFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf()
                }

                override fun changeParameter(filter: GlFilter, index: Int, value: Float) {
                }
            }

            return FilterItem("CGAColorSpace", builder)

        }

        private fun contrastFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GlFilter {
                    return GlContrastFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("contrast", 0, 0f, 4f, 0.01f, 1.2f),
                    )
                }

                override fun changeParameter(filter: GlFilter, index: Int, value: Float) {
                    val bf = filter as GlContrastFilter
                    bf.setContrast(value)
                }
            }

            return FilterItem("Contrast", builder)

        }

        private fun cornerFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GlFilter {
                    return GlCornerFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("LT", 0, 0f, 30f, 1f, 0f),
                            Parameter("RT", 1, 0f, 30f, 1f, 0f),
                            Parameter("LB", 2, 0f, 30f, 1f, 0f),
                            Parameter("RB", 3, 0f, 30f, 1f, 0f),
                    )
                }

                override fun changeParameter(filter: GlFilter, index: Int, value: Float) {
                    val bf = filter as GlCornerFilter
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
                override fun build(): GlFilter {
                    return GlCrosshatchFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("crossHatchSpacing", 0, 0f, 3f, 0.01f, 0.03f),
                            Parameter("lineWidth", 1, 0.001f, 2f, 0.001f, 0.003f)
                    )
                }

                override fun changeParameter(filter: GlFilter, index: Int, value: Float) {
                    val bf = filter as GlCrosshatchFilter
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
                override fun build(): GlFilter {
                    return GlExposureFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("exposure", 0, -10f, 10f, 0.1f, 1f),
                    )
                }

                override fun changeParameter(filter: GlFilter, index: Int, value: Float) {
                    val bf = filter as GlExposureFilter
                    bf.setExposure(value)
                }
            }

            return FilterItem("Exposure", builder)

        }

        //=========================================================
        private fun gammaFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GlFilter {
                    return GlGammaFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(Parameter("gamma", 0, 0f, 20f, 0.01f, 1.2f))
                }

                override fun changeParameter(filter: GlFilter, index: Int, value: Float) {
                    val bf = filter as GlGammaFilter
                    bf.gamma = value
                }
            }
            return FilterItem("Gamma", builder)
        }

        private fun gaussianBlurFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GlFilter {
                    return GlGaussianBlurFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("texelWidthOffset", 0, 0f, 1f, 0.001f, 0.01f),
                            Parameter("texelHeightOffset", 1, 0f, 1f, 0.001f, 0.01f),
                            Parameter("blurSize", 2, 0.1f, 3f, 0.1f, 0.2f),
                    )
                }

                override fun changeParameter(filter: GlFilter, index: Int, value: Float) {
                    val bf = filter as GlGaussianBlurFilter
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
                override fun build(): GlFilter {
                    return GlGaussianBlur2Filter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("samples", 0, 1f, 50f, 1f, 25f),
                            Parameter("scale", 1, 1f, 30f, 1f, 8f),
                    )
                }

                override fun changeParameter(filter: GlFilter, index: Int, value: Float) {
                    val bf = filter as GlGaussianBlur2Filter
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
                override fun build(): GlFilter {
                    return GlGaussianBlur3Filter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("blurSize", 0, 2f, 60f, 1f, 3f)
                    )
                }

                override fun changeParameter(filter: GlFilter, index: Int, value: Float) {
                    val bf = filter as GlGaussianBlur3Filter
                    bf.setBlurSize(value.toInt())
                }
            }
            return FilterItem("GaussianBlur3", builder)
        }

        private fun grayScaleFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GlFilter {
                    return GlGrayScaleFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf()
                }

                override fun changeParameter(filter: GlFilter, index: Int, value: Float) {
                }
            }
            return FilterItem("GrayScale", builder)
        }

        //========================================================

        private fun halfToneFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GlFilter {
                    return GlHalftoneFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("fractionalWidthOfPixel", 0, 0.01f, 1f, 0.01f, 0.01f),
                            Parameter("aspectRatio", 1, 0.1f, 10f, 0.1f, 1f),
                    )
                }

                override fun changeParameter(filter: GlFilter, index: Int, value: Float) {
                    val bf = filter as GlHalftoneFilter
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
                override fun build(): GlFilter {
                    return GlHazeFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("distance", 0, 0f, 1f, 0.01f, 0.2f),
                            Parameter("slope", 1, -1f, 1f, 0.01f, 0f),
                    )
                }

                override fun changeParameter(filter: GlFilter, index: Int, value: Float) {
                    val bf = filter as GlHazeFilter
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
                override fun build(): GlFilter {
                    return GlHighlightShadowFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("shadows", 0, 1f, 50f, 0.1f, 1f),
                            Parameter("highlights", 1, 0f, 1f, 0.01f, 0f),
                    )
                }

                override fun changeParameter(filter: GlFilter, index: Int, value: Float) {
                    val bf = filter as GlHighlightShadowFilter
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
                override fun build(): GlFilter {
                    return GlHueFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("hue", 0, 0f, 360f, 1f, 90f))
                }

                override fun changeParameter(filter: GlFilter, index: Int, value: Float) {
                    val bf = filter as GlHueFilter
                    bf.setHue(value)
                }
            }
            return FilterItem("Hue", builder)
        }

        //========================================================

        private fun invertFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GlFilter {
                    return GlInvertFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf()
                }

                override fun changeParameter(filter: GlFilter, index: Int, value: Float) {
                }
            }
            return FilterItem("Invert", builder)
        }

        private fun lookUpTableFilter(context: Context): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GlFilter {
                    val bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher_round)
                    return GlLookUpTableFilter(bitmap)
                }

                override fun getParameters(): List<Parameter> {
                    return listOf()
                }

                override fun changeParameter(filter: GlFilter, index: Int, value: Float) {
                }
            }
            return FilterItem("LookUpTable", builder)
        }

        private fun luminanceFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GlFilter {
                    return GlLuminanceFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf()
                }

                override fun changeParameter(filter: GlFilter, index: Int, value: Float) {
                }
            }
            return FilterItem("Luminance", builder)
        }

        private fun luminanceThresholdFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GlFilter {
                    return GlLuminanceThresholdFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("threshold", 0, 0f, 1f, 0.01f, 0.5f),
                    )
                }

                override fun changeParameter(filter: GlFilter, index: Int, value: Float) {
                    val bf = filter as GlLuminanceThresholdFilter
                    bf.setThreshold(value)
                }
            }
            return FilterItem("LuminanceThreshold", builder)
        }

        private fun monochromeFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GlFilter {
                    return GlMonochromeFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("intensity", 0, 0f, 1f, 0.01f, 1f),
                    )
                }

                override fun changeParameter(filter: GlFilter, index: Int, value: Float) {
                    val bf = filter as GlMonochromeFilter
                    bf.setIntensity(value)
                }
            }
            return FilterItem("Monochrome", builder)
        }

        private fun opacityFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GlFilter {
                    return GlOpacityFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("opacity", 0, 0f, 1f, 0.01f, 1f),
                    )
                }

                override fun changeParameter(filter: GlFilter, index: Int, value: Float) {
                    val bf = filter as GlOpacityFilter
                    bf.opacity = (value)
                }
            }
            return FilterItem("Opacity", builder)
        }

        private fun pixelationFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GlFilter {
                    return GlPixelationFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("pixel", 0, 1f, 100f, 1f, 1f),
                            Parameter("imageWidthFactor", 1, 0.001f, 1f, 0.001f, 0.0014f),
                            Parameter("imageHeightFactor", 2, 0.001f, 1f, 0.001f, 0.0014f),
                    )
                }

                override fun changeParameter(filter: GlFilter, index: Int, value: Float) {
                    val bf = filter as GlPixelationFilter
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
                override fun build(): GlFilter {
                    return GlPosterizeFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("colorLevels", 0, 1f, 256f, 1f, 10f),
                    )
                }

                override fun changeParameter(filter: GlFilter, index: Int, value: Float) {
                    val bf = filter as GlPosterizeFilter
                    bf.setColorLevels(value.toInt())
                }
            }
            return FilterItem("Posterize", builder)
        }

        private fun rgbFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GlFilter {
                    return GlRGBFilter()
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

                override fun changeParameter(filter: GlFilter, index: Int, value: Float) {
                    val bf = filter as GlRGBFilter
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
                override fun build(): GlFilter {
                    return GlSaturationFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("saturation", 0, 0f, 2f, 0.01f, 1f))
                }

                override fun changeParameter(filter: GlFilter, index: Int, value: Float) {
                    val bf = filter as GlSaturationFilter
                    bf.setSaturation(value)
                }
            }
            return FilterItem("Saturation", builder)
        }

        private fun sepiaFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GlFilter {
                    return GlSepiaFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf()
                }

                override fun changeParameter(filter: GlFilter, index: Int, value: Float) {
                }
            }
            return FilterItem("Sepia", builder)
        }

        private fun sharpenFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GlFilter {
                    return GlSharpenFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("sharpness", 0, 0f, 1f, 0.01f, 1f),
                    )
                }

                override fun changeParameter(filter: GlFilter, index: Int, value: Float) {
                    val bf = filter as GlSharpenFilter
                    bf.setsSharpness(value)
                }
            }
            return FilterItem("Sharpen", builder)
        }

        private fun sphereRefractionFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GlFilter {
                    return GlSphereRefractionFilter()
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

                override fun changeParameter(filter: GlFilter, index: Int, value: Float) {
                    val bf = filter as GlSphereRefractionFilter
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

        private fun swirlFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GlFilter {
                    return GlSwirlFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("centerX", 0, 0f, 1f, 0.01f, 0.5f),
                            Parameter("centerY", 1, 0f, 1f, 0.01f, 0.5f),
                            Parameter("radius", 2, 0f, 1f, 0.01f, 0.5f),
                            Parameter("angle", 3, 0f, 2f, 0.01f, 1f),
                    )
                }

                override fun changeParameter(filter: GlFilter, index: Int, value: Float) {
                    val bf = filter as GlSwirlFilter
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
                override fun build(): GlFilter {
                    return GlThreex3TextureSamplingFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf()
                }

                override fun changeParameter(filter: GlFilter, index: Int, value: Float) {
                }
            }
            return FilterItem("ThreeX3Sampling", builder)
        }

        private fun toneFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GlFilter {
                    return GlToneFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("threshold", 0, 0f, 2f, 0.01f, 0.2f),
                            Parameter("quantizationLevels", 1, 1f, 100f, 1f, 10f),
                    )
                }

                override fun changeParameter(filter: GlFilter, index: Int, value: Float) {
                    val bf = filter as GlToneFilter
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
                override fun build(): GlFilter {
                    return GlVibranceFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("vibrance", 0, 0f, 2f, 0.01f, 0f),
                    )
                }

                override fun changeParameter(filter: GlFilter, index: Int, value: Float) {
                    val bf = filter as GlVibranceFilter
                    bf.setVibrance(value)
                }
            }
            return FilterItem("Vibrance", builder)
        }

        private fun vignetterFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GlFilter {
                    return GlVignetteFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("centerX", 0, 0f, 1f, 0.01f, 0.5f),
                            Parameter("centerY", 1, 0f, 1f, 0.01f, 0.5f),
                            Parameter("vignetteStart", 2, 0f, 1f, 0.01f, 0.2f),
                            Parameter("vignetteEnd", 3, 0f, 1f, 0.01f, 0.85f))
                }

                override fun changeParameter(filter: GlFilter, index: Int, value: Float) {
                    val bf = filter as GlVignetteFilter
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
                override fun build(): GlFilter {
                    val bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher_round)
                    return GlWatermarkFilter(bitmap)
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("position", 0, 0f, 3f, 1f, 0f),
                    )
                }

                override fun changeParameter(filter: GlFilter, index: Int, value: Float) {
                    val bf = filter as GlWatermarkFilter
                    when (value.toInt()) {
                        0 -> bf.position = GlWatermarkFilter.Position.LEFT_TOP
                        1 -> bf.position = GlWatermarkFilter.Position.RIGHT_TOP
                        2 -> bf.position = GlWatermarkFilter.Position.LEFT_BOTTOM
                        3 -> bf.position = GlWatermarkFilter.Position.RIGHT_BOTTOM
                    }
                }
            }
            return FilterItem("WaterMark", builder)
        }

        private fun weakPixelInclusionFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GlFilter {
                    return GlWeakPixelInclusionFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf()
                }

                override fun changeParameter(filter: GlFilter, index: Int, value: Float) {}
            }
            return FilterItem("WeakPixelInclusion", builder)
        }

        private fun whiteBalanceFilter(): FilterItem {
            val builder = object : FilterBuilder {
                override fun build(): GlFilter {
                    return GlWhiteBalanceFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("temperature", 0, 1000f, 20000f, 100f, 5000f),
                            Parameter("tint", 1, -100f, 100f, 1f, 0f))
                }

                override fun changeParameter(filter: GlFilter, index: Int, value: Float) {
                    val bf = filter as GlWhiteBalanceFilter
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
                override fun build(): GlFilter {
                    return GlZoomBlurFilter()
                }

                override fun getParameters(): List<Parameter> {
                    return listOf(
                            Parameter("centerX", 0, 0f, 1f, 0.01f, 0.5f),
                            Parameter("centerY", 1, 0f, 1f, 0.01f, 0.5f),
                            Parameter("blurSize", 2, 1f, 25f, 1f, 1f))
                }

                override fun changeParameter(filter: GlFilter, index: Int, value: Float) {
                    val bf = filter as GlZoomBlurFilter
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