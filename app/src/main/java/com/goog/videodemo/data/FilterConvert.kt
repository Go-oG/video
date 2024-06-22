package com.goog.videodemo.data


import com.goog.effect.filter.core.GLFilter
import com.goog.effect.model.CallBy
import com.goog.effect.model.FloatDelegate
import com.goog.effect.model.IntDelegate
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier

object FilterConvert {
    private val methodWeightMap = mapOf(
        "iteratorcount" to 0,
        "blursize" to 1,

        "sample" to 2,
        "samples" to 2,

        "centerx" to 3,
        "centery" to 4,

        "angle" to 5,
        "anglex" to 6,
        "angley" to 7,

        "topleft" to 8,
        "topright" to 9,
        "bottomleft" to 10,
        "bottomright" to 11,
        "lefttop" to 8,
        "leftbottom" to 9,
        "righttop" to 10,
        "rightbottom" to 11,

        "texelheightoffset" to 12,
        "texelwidthoffset" to 13,

        "radius" to 14,
        "strength" to 15,
        "bluramount" to 16,
        "aspect" to 17,
        "scale" to 18,
        "smoothing" to 19,
        "threshold" to 20,
        "intensity" to 21,
        "brightness" to 22,
        "contrast" to 23,
        "crosshatch" to 24,
        "linewidth" to 25,
        "index" to 26,
        "fractional" to 27
    )

    /**
     * 给定类名解析出参数列表和相关数据
     */
    fun parse(cls: Class<*>): FilterItem {
        val parameterList = mutableListOf<Parameter>()
        val filterItem = FilterItem(getShowName(cls), cls, parameterList)

        ///获取所有公共方法
        val methodSets = mutableSetOf<Method>()
        for (method in cls.methods) {
            method.isAccessible = true
            val mode = method.modifiers
            if (!Modifier.isPublic(mode)) {
                continue
            }
            val methodName = method.name
            if (!methodName.startsWith("set")) {
                continue
            }
            if (method.parameterCount != 1) {
                continue
            }
            val pType = method.parameterTypes.first()
            if (pType == Float::class.java || pType == Int::class.java) {
                methodSets.add(method)
            }
        }
        val filter = createFilterByClass(cls)

        for (method in methodSets) {
            val para = buildParameterByMethod(filter, cls, method)
            if (para != null) {
                parameterList.add(para)
            }
        }
        (filter as GLFilter).release(CallBy.DESTROY)
        return filterItem
    }

    private fun buildParameterByMethod(obj: Any, cls: Class<*>, method: Method): Parameter? {
        return buildForNormal(obj, method, cls) ?: buildForDelegate(method, cls, obj)
    }

    private fun createFilterByClass(cls: Class<*>): Any {
        val tmpList = cls.constructors
        if (tmpList.isNullOrEmpty()) {
            throw IllegalArgumentException("class must have a public constructor")
        }
        val list = tmpList.toMutableList()
        list.sortWith { a1, a2 ->
            a1.isAccessible = true
            a2.isAccessible = true
            val p1 = if (Modifier.isPublic(a1.modifiers)) 0 else 100
            val p2 = if (Modifier.isPublic(a2.modifiers)) 0 else 100
            return@sortWith (p1 + a1.parameterTypes.size).compareTo(p2 + a2.parameterTypes.size)
        }
        //TODO 暂时使用无参构造函数
        val constructor = list.first()
        return constructor.newInstance()
    }

    private fun getShowName(cls: Class<*>): String {
        var showName = cls.simpleName
        if (showName.startsWith("gl", ignoreCase = true)) {
            showName = showName.substring(2)
        }
        if (showName.endsWith("filter", ignoreCase = true)) {
            showName = showName.substring(0, showName.length - "filter".length)
        }
        return showName
    }

    private fun buildForDelegate(method: Method, cls: Class<*>, filter: Any): Parameter? {
        var s = method.name.substring(3)
        s = s.replaceFirstChar {
            it.lowercaseChar()
        }
        s = "$s\$delegate"
        val fdcls = FloatDelegate::class.java
        val idcls = IntDelegate::class.java
        try {
            val field = cls.getDeclaredField(s)
            field.isAccessible = true
            val type = field.type
            if (type == fdcls) {
                val obj = field.get(filter)
                val value = obj as FloatDelegate
                val minV = value.minV ?: -10f
                val maxV = value.maxV ?: (minV.coerceAtLeast(10f))
                val step = (maxV - minV) / 100f
                return Parameter(s, method.name, minV, maxV, step, true).apply {
                    curValue = value.getCurrent()
                }
            }
            if (type == idcls) {
                val obj = field.get(filter)
                val value = obj as IntDelegate
                val minV = value.minV ?: -10
                val maxV = value.maxV ?: (kotlin.math.max(minV, 10))
                val step = (maxV - minV) / 100
                return Parameter(s, method.name, minV.toFloat(), maxV.toFloat(), step.toFloat(), false).apply {
                    curValue = value.getCurrent().toFloat()
                }
            }
        } catch (e: Exception) {
            return null
        }
        return null
    }

    private fun buildForNormal(obj: Any, method: Method, cls: Class<*>): Parameter? {
        try {
            val pType = method.parameterTypes.first()
            val useFloat = pType == Float::class.java
            var showName = method.name
            if (showName.startsWith("set")) {
                showName = showName.substring(3)
            }
            showName = showName.firstLowCase()
            val tryFieldName = "$showName\$delegate"
            val field =
                getFieldByName(cls, tryFieldName) ?: return Parameter(
                    showName, method.name, 0f, 100f,
                    0.1f, useFloat
                )
            return buildParameterByField(obj, cls, field, showName, method.name)
        } catch (e: Exception) {
            return null
        }
    }

    private fun buildParameterByField(
        obj: Any, cls: Class<*>, field: Field, showName: String,
        methodName: String
    ): Parameter? {
        if (field.type == FloatDelegate::class.java) {
            val delegate = (field.get(obj) as FloatDelegate)
            val cur = delegate.getCurrent()
            var minValue = delegate.minV
            var maxValue = delegate.maxV
            if (minValue == null && maxValue == null) {
                minValue = cur - 100
                maxValue = cur + 100
            } else if (minValue == null && maxValue != null) {
                minValue = cur - 100
            } else if (minValue != null && maxValue == null) {
                maxValue = cur + 100
            }
            val step = (maxValue!! - minValue!!) / 100f
            return Parameter(showName, methodName, minValue, maxValue, step, true)
        }

        return null
    }


    private fun getFieldByName(cls: Class<*>, fieldName: String): Field? {
        var field: Field? = null
        try {
            field = cls.getField(fieldName)
            if (field == null) {
                field = cls.getDeclaredField(fieldName)
            }
        } catch (e: Exception) {
            try {
                field = cls.getDeclaredField(fieldName)
            } catch (_: Exception) {
            }
        }
        return field
    }


}