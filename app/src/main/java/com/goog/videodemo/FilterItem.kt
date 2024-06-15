package com.goog.videodemo

import android.content.Context
import com.goog.effect.filter.GLAdaptiveThresholdFilter
import com.goog.effect.filter.GLAlphaFrameFilter
import com.goog.effect.filter.GLAverageColorFilter
import com.goog.effect.filter.GLBilateralFilter
import com.goog.effect.filter.GLBrightnessFilter
import com.goog.effect.filter.GLBulgeDistortionFilter
import com.goog.effect.filter.GLCGAColorSpaceFilter
import com.goog.effect.filter.GLChromaKeyFilter
import com.goog.effect.filter.GLColorFASTDescriptorFilter
import com.goog.effect.filter.GLColorMatrixFilter
import com.goog.effect.filter.GLColorSwizzlingFilter
import com.goog.effect.filter.GLContrastFilter
import com.goog.effect.filter.GLConvolution3X3Filter
import com.goog.effect.filter.GLCornerFilter
import com.goog.effect.filter.GLCrosshatchFilter
import com.goog.effect.filter.GLDirectionNonMaxSuppressionFilter
import com.goog.effect.filter.GLDirectionSobelEdgeDetectionFilter
import com.goog.effect.filter.GLExposureFilter
import com.goog.effect.filter.GLGammaFilter
import com.goog.effect.filter.GLGlassSphereFilter
import com.goog.effect.filter.GLGrayScaleFilter
import com.goog.effect.filter.GLHalftoneFilter
import com.goog.effect.filter.GLHighLightShadowTintFilter
import com.goog.effect.filter.GLHighlightShadowFilter
import com.goog.effect.filter.GLHistogramBlueSamplingFilter
import com.goog.effect.filter.GLHistogramDisplayFilter
import com.goog.effect.filter.GLHistogramEqualizationGreenFilter
import com.goog.effect.filter.GLHistogramEqualizationLuminanceFilter
import com.goog.effect.filter.GLHistogramEqualizationRGBFilter
import com.goog.effect.filter.GLHistogramEqualizationRedFilter
import com.goog.effect.filter.GLHistogramRedSamplingFilter
import com.goog.effect.filter.GLHueBlendFilter
import com.goog.effect.filter.GLHueFilter
import com.goog.effect.filter.GLInvertFilter
import com.goog.effect.filter.GLKuwaharaFilter
import com.goog.effect.filter.GLKuwaharaRadius3Filter
import com.goog.effect.filter.GLLanczosResamplingFilter
import com.goog.effect.filter.GLLaplacianFilter
import com.goog.effect.filter.GLLevelsFilter
import com.goog.effect.filter.GLLocalBinaryPatternFilter
import com.goog.effect.filter.GLLookupFilter
import com.goog.effect.filter.GLLuminanceFilter
import com.goog.effect.filter.GLLuminanceRangeFilter
import com.goog.effect.filter.GLLuminanceThresholdFilter
import com.goog.effect.filter.GLMonochromeFilter
import com.goog.effect.filter.GLNobleCornerDetectorFilter
import com.goog.effect.filter.GLOpacityFilter
import com.goog.effect.filter.GLPassthroughFilter
import com.goog.effect.filter.GLPinchDistortionFilter
import com.goog.effect.filter.GLPixelateFilter
import com.goog.effect.filter.GLPolarPixelateFilter
import com.goog.effect.filter.GLPolkaDotFilter
import com.goog.effect.filter.GLPosterizeFilter
import com.goog.effect.filter.GLRGBFilter
import com.goog.effect.filter.GLSaturationFilter
import com.goog.effect.filter.GLScaleFilter
import com.goog.effect.filter.GLSepiaFilter
import com.goog.effect.filter.GLSharpenFilter
import com.goog.effect.filter.GLShiTomasiFeatureDetectorFilter
import com.goog.effect.filter.GLSketchFilter
import com.goog.effect.filter.GLSobelEdgeDetectionFilter
import com.goog.effect.filter.GLSolarizeFilter
import com.goog.effect.filter.GLSphereRefractionFilter
import com.goog.effect.filter.GLStretchDistortionFilter
import com.goog.effect.filter.GLSwirlFilter
import com.goog.effect.filter.GLThresholdEdgeDetectionFilter
import com.goog.effect.filter.GLThresholdedNonMaximumSuppressionFilter
import com.goog.effect.filter.GLToneFilter
import com.goog.effect.filter.GLUnsharpMaskFilter
import com.goog.effect.filter.GLVibranceFilter
import com.goog.effect.filter.GLVignetteFilter
import com.goog.effect.filter.GLWeakPixelInclusionFilter
import com.goog.effect.filter.GLWhiteBalanceFilter
import com.goog.effect.filter.GLXyDerivativeFilter
import com.goog.effect.filter.GLYuvConversionFullRangeFilter
import com.goog.effect.filter.GLYuvConversionFullRangeUVPlanarFilter
import com.goog.effect.filter.GLYuvConversionVideoRangeFilter
import com.goog.effect.filter.blend.GLAddBlendFilter
import com.goog.effect.filter.blend.GLChromaKeyBlendFilter
import com.goog.effect.filter.blur.GLBoxBlurFilter
import com.goog.effect.filter.blur.GLDirectionBlurFilter
import com.goog.effect.filter.blur.GLDualKawaseBlurFilter
import com.goog.effect.filter.blur.GLGaussianBlurFilter
import com.goog.effect.filter.blur.GLGrainyBlurFilter
import com.goog.effect.filter.blur.GLRadialBlurFilter
import com.goog.effect.filter.blur.GLZoomBlurFilter
import com.goog.effect.filter.core.GLFilter
import com.goog.videodemo.ParameterBuilder.loadParameters
import java.util.Locale

class Parameter(
    val varName: String,
    val setMethodName: String,
    val minValue: Float,
    val maxValue: Float,
    val step: Float,
    var curValue: Float, val useFloat: Boolean
) {
    val showName: String
        get() {
            return varName.replace("\$delegate", "")
        }
}

class FilterItem(val clsName: Class<*>) {
    val parameter = loadParameters(clsName)
    var filter: GLFilter? = null
    var select = false

    val name: String
        get() {
            var itemName = clsName.simpleName.replace(".kt", "")
            itemName = itemName.replace("GL", "")
            itemName = itemName.replace("Filter", "")
            return itemName
        }

    fun select() {
        select = true
        filter = clsName.newInstance() as GLFilter
    }

    fun unselect() {
        select = false
        filter = null
    }

    fun changeParameter(parameter: Parameter, value: Float) {
        val filter = this.filter ?: return
        val cls = filter::class.java
        val methodName = parameter.setMethodName
        val method = if (parameter.useFloat) cls.getMethod(methodName, Float::class.java) else cls.getMethod(methodName, Int::class.java)
        method.isAccessible = true
        if (parameter.useFloat) {
            method.invoke(filter, value)
        } else {
            method.invoke(filter, value.toInt())
        }
        parameter.curValue = value
    }

    companion object {
        fun loadFiltersData(context: Context): List<FilterItem> {
            val list = mutableListOf<FilterItem>()

            list.add(FilterItem(GLDualKawaseBlurFilter::class.java))

            list.add(FilterItem(GLGrainyBlurFilter::class.java))
            list.add(FilterItem(GLDirectionBlurFilter::class.java))

            list.add(FilterItem(GLRadialBlurFilter::class.java))

            //============================
            list.add(FilterItem(GLAdaptiveThresholdFilter::class.java))
            list.add(FilterItem(GLAlphaFrameFilter::class.java))
            list.add(FilterItem(GLAverageColorFilter::class.java))

            //  list.add(FilterItem(GLAverageLuminanceFilter::class.java))

            list.add(FilterItem(GLBilateralFilter::class.java))
            //  list.add(FilterItem(GLBilateralBlurFilter::class.java))
            list.add(FilterItem(GLBoxBlurFilter::class.java))
            list.add(FilterItem(GLBrightnessFilter::class.java))
            list.add(FilterItem(GLBulgeDistortionFilter::class.java))

            list.add(FilterItem(GLCGAColorSpaceFilter::class.java))
            list.add(FilterItem(GLChromaKeyFilter::class.java))
            //  list.add(FilterItem(GLCircleFilter::class.java))
            list.add(FilterItem(GLColorFASTDescriptorFilter::class.java))
            //  list.add(FilterItem(GLColorLocalNonaryPatternFilter::class.java))
            list.add(FilterItem(GLColorMatrixFilter::class.java))
            list.add(FilterItem(GLColorSwizzlingFilter::class.java))

            list.add(FilterItem(GLContrastFilter::class.java))
            list.add(FilterItem(GLConvolution3X3Filter::class.java))
            list.add(FilterItem(GLCornerFilter::class.java))
            // list.add(FilterItem(GLCrosshairFilter::class.java))
            list.add(FilterItem(GLCrosshatchFilter::class.java))

            //   list.add(FilterItem(GLDilationFilter::class.java))
            list.add(FilterItem(GLDirectionNonMaxSuppressionFilter::class.java))
            list.add(FilterItem(GLDirectionSobelEdgeDetectionFilter::class.java))
            //   list.add(FilterItem(GLErosionFilter::class.java))
            list.add(FilterItem(GLExposureFilter::class.java))

            //   list.add(FilterItem(GLFalseColorFilter::class.java))
            list.add(FilterItem(GLGammaFilter::class.java))

            list.add(FilterItem(GLGaussianBlurFilter::class.java))
            list.add(FilterItem(GLGlassSphereFilter::class.java))
            list.add(FilterItem(GLGrayScaleFilter::class.java))

            list.add(FilterItem(GLHalftoneFilter::class.java))
            list.add(FilterItem(GLHighlightShadowFilter::class.java))
            list.add(FilterItem(GLHighLightShadowTintFilter::class.java))
            // list.add(FilterItem(GLHistogramAccumulationFilter::class.java))
            list.add(FilterItem(GLHistogramBlueSamplingFilter::class.java))
            list.add(FilterItem(GLHistogramDisplayFilter::class.java))
            list.add(FilterItem(GLHistogramEqualizationGreenFilter::class.java))
            list.add(FilterItem(GLHistogramEqualizationLuminanceFilter::class.java))
            list.add(FilterItem(GLHistogramEqualizationRedFilter::class.java))
            list.add(FilterItem(GLHistogramEqualizationRGBFilter::class.java))
            //      list.add(FilterItem(GLHistogramGreenSamplingFilter::class.java))
            //     list.add(FilterItem(GLHistogramLuminanceSamplingFilter::class.java))
            list.add(FilterItem(GLHistogramRedSamplingFilter::class.java))
            list.add(FilterItem(GLHueBlendFilter::class.java))
            list.add(FilterItem(GLHueFilter::class.java))

            list.add(FilterItem(GLInvertFilter::class.java))

            list.add(FilterItem(GLKuwaharaFilter::class.java))
            list.add(FilterItem(GLKuwaharaRadius3Filter::class.java))

            list.add(FilterItem(GLLanczosResamplingFilter::class.java))
            list.add(FilterItem(GLLaplacianFilter::class.java))

            list.add(FilterItem(GLLevelsFilter::class.java))
            //  list.add(FilterItem(GLLineFilter::class.java))
            list.add(FilterItem(GLLocalBinaryPatternFilter::class.java))
            list.add(FilterItem(GLLookupFilter::class.java))
            //  list.add(FilterItem(GLLookUpTableFilter::class.java))
            list.add(FilterItem(GLLuminanceFilter::class.java))
            list.add(FilterItem(GLLuminanceRangeFilter::class.java))
            list.add(FilterItem(GLLuminanceThresholdFilter::class.java))

            //   list.add(FilterItem(GLMedianFilter::class.java))
            list.add(FilterItem(GLMonochromeFilter::class.java))
            //  list.add(FilterItem(GLMotionComparisonFilter::class.java))

            list.add(FilterItem(GLNobleCornerDetectorFilter::class.java))

            list.add(FilterItem(GLOpacityFilter::class.java))

            list.add(FilterItem(GLPassthroughFilter::class.java))
            list.add(FilterItem(GLPinchDistortionFilter::class.java))
            list.add(FilterItem(GLPixelateFilter::class.java))
            list.add(FilterItem(GLPolarPixelateFilter::class.java))
            list.add(FilterItem(GLPolkaDotFilter::class.java))
            list.add(FilterItem(GLPosterizeFilter::class.java))

            list.add(FilterItem(GLRGBFilter::class.java))

            list.add(FilterItem(GLSaturationFilter::class.java))
            list.add(FilterItem(GLScaleFilter::class.java))

            list.add(FilterItem(GLSepiaFilter::class.java))
            list.add(FilterItem(GLSharpenFilter::class.java))
            list.add(FilterItem(GLShiTomasiFeatureDetectorFilter::class.java))
            list.add(FilterItem(GLSketchFilter::class.java))
            list.add(FilterItem(GLSobelEdgeDetectionFilter::class.java))
            list.add(FilterItem(GLSolarizeFilter::class.java))
            list.add(FilterItem(GLSphereRefractionFilter::class.java))
            list.add(FilterItem(GLStretchDistortionFilter::class.java))
            //   list.add(FilterItem(GLSuperResolutionFilter::class.java))
            list.add(FilterItem(GLSwirlFilter::class.java))

            //     list.add(FilterItem(GLThreex3TextureSamplingFilter::class.java))
            list.add(FilterItem(GLThresholdEdgeDetectionFilter::class.java))
            list.add(FilterItem(GLThresholdedNonMaximumSuppressionFilter::class.java))
            //   list.add(FilterItem(GLThresholdSketchFilter::class.java))
            //  list.add(FilterItem(GLTiltShiftFilter::class.java))

            //  list.add(FilterItem(GLToneCurveFilter::class.java))

            list.add(FilterItem(GLToneFilter::class.java))
            //  list.add(FilterItem(GLTransformFilter::class.java))

            list.add(FilterItem(GLUnsharpMaskFilter::class.java))

            list.add(FilterItem(GLVibranceFilter::class.java))
            list.add(FilterItem(GLVignetteFilter::class.java))

            //   list.add(FilterItem(GLWatermarkFilter::class.java))

            list.add(FilterItem(GLWeakPixelInclusionFilter::class.java))
            list.add(FilterItem(GLWhiteBalanceFilter::class.java))

            list.add(FilterItem(GLXyDerivativeFilter::class.java))

            list.add(FilterItem(GLYuvConversionFullRangeFilter::class.java))
            list.add(FilterItem(GLYuvConversionFullRangeUVPlanarFilter::class.java))
            list.add(FilterItem(GLYuvConversionVideoRangeFilter::class.java))

            list.add(FilterItem(GLZoomBlurFilter::class.java))

            //Blend
            list.add(FilterItem(GLAddBlendFilter::class.java))
            //  list.add(FilterItem(GLAlphaBlendFilter::class.java))
            list.add(FilterItem(GLChromaKeyBlendFilter::class.java))
            // list.add(FilterItem(GLColorBlendFilter::class.java))
            //  list.add(FilterItem(GLColorBurnBlendFilter::class.java))
            //   list.add(FilterItem(GLColorDodgeBlendFilter::class.java))

            // list.add(FilterItem(GLDarkenBlendFilter::class.java))
            // list.add(FilterItem(GLDifferenceBlendFilter::class.java))
//            list.add(FilterItem(GLDissolveBlendFilter::class.java))
//            list.add(FilterItem(GLDivideBlendFilter::class.java))
//
//            list.add(FilterItem(GLExclusionBlendFilter::class.java))
//
//            list.add(FilterItem(GLHardLightBlendFilter::class.java))
//            list.add(FilterItem(GLHistogramEqualizationBlueFilter::class.java))
//
//            list.add(FilterItem(GLLightenBlendFilter::class.java))
//            list.add(FilterItem(GLLinearBurnBlendFilter::class.java))
//            list.add(FilterItem(GLLuminosityBlendFilter::class.java))
//
//            list.add(FilterItem(GLMultiplyBlendFilter::class.java))
//
//            list.add(FilterItem(GLNormalBlendFilter::class.java))
//            list.add(FilterItem(GLOverlayBlendFilter::class.java))
//            list.add(FilterItem(GLSaturationBlendFilter::class.java))
//            list.add(FilterItem(GLScreenBlendFilter::class.java))
//            list.add(FilterItem(GLSoftLightBlendFilter::class.java))
//            list.add(FilterItem(GLSourceOverBlendFilter::class.java))
//            list.add(FilterItem(GLSubtractBlendFilter::class.java))

            return list
        }
    }
}

fun String.firstUpCase(): String {
    if (isBlank()) {
        return ""
    }
    return replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
    }
}

fun String.firstLowCase(): String {
    if (isBlank()) {
        return ""
    }
    return replaceFirstChar {
        if (it.isUpperCase()) it.lowercase() else it.toString()
    }
}










