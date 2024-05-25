package com.goog.videodemo

import com.goog.video.filter.core.GLFilter
import com.goog.video.model.FloatDelegate
import com.goog.video.model.IntDelegate
import java.lang.Math.max
import java.lang.reflect.Modifier
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

object ParameterBuilder {

    fun loadParameters(cls: Class<*>): List<Parameter> {
        val methods = cls.declaredMethods
        if (methods.isEmpty()) {
            return listOf()
        }
        val setSets = mutableSetOf<String>()
        for (item in methods) {
            item.isAccessible = true
            val mode = item.modifiers
            if (!Modifier.isPublic(mode)) {
                continue
            }
            val name = item.name
            if (!name.startsWith("set")) {
                continue
            }
            if (item.parameterCount != 1) {
                continue
            }
            for (type in item.parameterTypes) {
                if (type == Float::class.java || type == Int::class.java) {
                    setSets.add(name)
                }
                break
            }
        }

        val parameterList = mutableListOf<Parameter>()
        val filter = cls.newInstance()

        for (method in setSets) {
            val para = buildForDelegate(method, cls, filter) ?: buildForNormal(method, cls)
            if (para != null) {
                parameterList.add(para)
            }
        }
        (filter as GLFilter).release()
        return parameterList
    }

    private fun buildForDelegate(method: String, cls: Class<*>, filter: Any): Parameter? {
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
                return Parameter(s, method, minV.toFloat(), maxV.toFloat(), step.toFloat(), value.getCurrent().toFloat(), false)
            }
        } catch (e: Exception) {
            return null
        }
        return null
    }

    private fun buildForNormal(methodName: String, cls: Class<*>): Parameter? {
        try {
            var useFloat = true
            val method = try {
                cls.getMethod(methodName, Float::class.java)
            } catch (e: Exception) {
                useFloat = false
                cls.getMethod(methodName, Int::class.java)
            }
            method.isAccessible = true
            val varName = methodName.substring(3).firstLowCase()
            return Parameter(varName, methodName, 0f, 100f, 1f, 1f, useFloat)
        } catch (e: Exception) {
            return null
        }
    }


}