package com.goog.effect.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * Provides access to a global application [Context].
 *
 * The recommended way is to call [init] from your Application.onCreate():
 * ```
 * class MyApp : Application() {
 *     override fun onCreate() {
 *         super.onCreate()
 *         ContextUtil.init(this)
 *     }
 * }
 * ```
 * If [init] was not called, a reflection-based fallback attempts to
 * locate the Application instance automatically (may not work on all
 * Android versions or in test environments).
 */
@SuppressLint("StaticFieldLeak")
object ContextUtil {

    @Volatile
    private var mContext: Context? = null

    /**
     * Initialize with an application context. Call this early from
     * Application.onCreate() for the most reliable setup.
     */
    fun init(context: Context) {
        mContext = context.applicationContext
    }

    /**
     * @deprecated Use [init] instead.
     */
    @Deprecated("Use init() instead", ReplaceWith("init(context)"))
    fun initContext(c: Context?) {
        if (c != null) {
            init(c)
        }
    }

    fun getContext(): Context {
        mContext?.let { return it }
        return resolveViaReflection()
    }

    @Suppress("PrivateApi")
    private fun resolveViaReflection(): Context {
        try {
            val runtimeInit = Class.forName("com.android.internal.os.RuntimeInit")
            val appObjectField = runtimeInit.getDeclaredField("mApplicationObject")
            appObjectField.isAccessible = true
            val appObject = appObjectField.get(runtimeInit)

            val appThreadClass = Class.forName("android.app.ActivityThread\$ApplicationThread")
            val thisField = appThreadClass.getDeclaredField("this$0")
            thisField.isAccessible = true
            val activityThread = thisField.get(appObject)

            val atClass = Class.forName("android.app.ActivityThread")
            val getAppMethod = atClass.getMethod("getApplication")
            getAppMethod.isAccessible = true
            val app = getAppMethod.invoke(activityThread) as Application

            mContext = app
            return app
        } catch (e: Exception) {
            throw IllegalStateException(
                "ContextUtil has not been initialized. " +
                "Call ContextUtil.init(applicationContext) from your Application.onCreate().",
                e
            )
        }
    }
}
