package com.goog.video.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

@SuppressLint("StaticFieldLeak,PrivateApi")
object ContextUtil {

    private var application: Application? = null
        get() {
            if (field != null) {
                return field
            }
            try {
                val localClass1 = Class.forName("com.android.internal.os.RuntimeInit")
                val localField1 = localClass1.getDeclaredField("mApplicationObject")
                localField1.isAccessible = true
                val localObject1 = localField1[localClass1]

                val localClass2 = Class.forName("android.app.ActivityThread\$ApplicationThread")
                val localField2 = localClass2.getDeclaredField("this$0")
                localField2.isAccessible = true
                val localObject2 = localField2[localObject1]

                val localClass3 = Class.forName("android.app.ActivityThread")
                val localMethod = localClass3.getMethod("getApplication", *arrayOfNulls(0))
                localMethod.isAccessible = true
                val localApplication = localMethod.invoke(localObject2, *arrayOfNulls(0)) as Application
                field = localApplication
            } catch (localException: Exception) {
                localException.printStackTrace()
            }
            return field
        }

    private var mContext: Context? = null

    fun initContext(c: Context?) {
        mContext = c?.applicationContext
    }

    fun getContext(): Context {
        var c = mContext
        if (c != null) {
            return c
        }
        c = application!!
        mContext = c
        return c
    }
}