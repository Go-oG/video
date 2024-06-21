package com.goog.videodemo.data

import androidx.core.text.buildSpannedString
import com.goog.effect.filter.core.GLFilter
import com.goog.effect.model.CallBy
import com.goog.effect.model.FloatDelegate
import com.goog.effect.model.IntDelegate
import java.lang.Math.addExact
import java.lang.Math.max
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier

object FilterConvert {

    private val methodNameList = listOf(
            "iteratorcount",
            "iterationcount",
            "blursize",
            "blurradius",
    )


    /**
     * 给定类名解析出参数列表和相关数据
     */
    fun <T : GLFilter> parse(cls: Class<T>): FilterItem {
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

        val allField = getFiles(cls)

        for (method in methodSets) {
            val para = buildParameterByMethod(filter, cls, method, allField)
            if(para!=null){
                parameterList.add(para)
            }
        }
        (filter as GLFilter).release(CallBy.DESTROY)
        return filterItem
    }

    private fun buildParameterByMethod(obj: Any, cls: Class<*>, method: Method,
        fieldList: Map<Class<*>, Set<Field>>): Parameter? {
        return buildForNormal(method, cls, fieldList) ?: buildForDelegate(method, cls, obj, fieldList)
    }




    ///获取所有的属性字段
    //public private protected
    private fun getFiles(cls: Class<*>): Map<Class<*>, Set<Field>> {
        ///先处理公共的
        var nextCls: Class<*>? = cls
        val fieldMap = mutableMapOf<Class<*>, Set<Field>>()
        while (nextCls != null) {
            val tmpList = nextCls.declaredFields
            if (tmpList.isNullOrEmpty()) {
                nextCls = nextCls.superclass
                continue
            }
            val fieldSet = mutableSetOf<Field>()
            fieldSet.addAll(tmpList)
            fieldMap[cls] = fieldSet
            if (nextCls == GLFilter::class.java) {
                break
            }
            nextCls = nextCls.superclass
        }
        return fieldMap
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
            return@sortWith (p1 + a1.parameterCount).compareTo(p2 + a2.parameterCount)
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


    private fun buildForDelegate(method: Method, cls: Class<*>, filter: Any,
        fieldList: Map<Class<*>, Set<Field>>): Parameter? {
        var s = method.substring(3)
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
                val maxV = value.maxV ?: (max(minV, 10f))
                val step = (maxV - minV) / 100f
                return Parameter(s, method, minV, maxV, step, value.getCurrent(), true)

            }
            if (type == idcls) {
                val obj = field.get(filter)
                val value = obj as IntDelegate
                val minV = value.minV ?: -10
                val maxV = value.maxV ?: (kotlin.math.max(minV, 10))
                val step = (maxV - minV) / 100
                return Parameter(s, method, minV.toFloat(), maxV.toFloat(), step.toFloat(),
                        value.getCurrent().toFloat(), false)
            }
        } catch (e: Exception) {
            return null
        }
        return null
    }

    private fun buildForNormal(obj: Any, method: Method, cls: Class<*>,
        fieldList: Map<Class<*>, Set<Field>>): Parameter? {
        try {
            val pType = method.parameterTypes.first()
            val useFloat = pType == Float::class.java
            var showName = method.name
            if (showName.startsWith("set")) {
                showName = showName.substring(2)
            }
            showName = showName.firstLowCase()
            val tryFieldName = "$showName\$delegate"
            val field =
                getFieldByName(cls, tryFieldName) ?: return Parameter(showName, method.name, 0f, 100f,
                        0.1f, useFloat)

            return buildParameterByField(obj, cls, field, showName, method.name)

        } catch (e: Exception) {
            return null
        }
    }

    private fun buildParameterByField(obj: Any, cls: Class<*>, field: Field, showName: String,
        methodName: String): Parameter? {
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