package com.mvvm.utilities

import android.util.Log
import com.mvvm.BuildConfig

class LogUtils {
    companion object {

        private const val LOG_PREFIX = PREF_DEMO_APP
        private const val LOG_PREFIX_LENGTH = LOG_PREFIX.length
        private const val MAX_LOG_TAG_LENGTH = 100

        private fun makeLogTag(str: String?): String? {
            return if (str != null && str.length > MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH) {
                LOG_PREFIX + str.substring(0, MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH - 1)
            } else LOG_PREFIX + str
        }

        fun logV(tag: String?, message: String?) {
            if (BuildConfig.DEBUG && message != null) {
                Log.v(makeLogTag(tag), message)
            }
        }

        fun logV(
            tag: String?, message: String?, cause: Throwable?
        ) {
            if (BuildConfig.DEBUG && message != null && cause != null) {
                Log.v(makeLogTag(tag), message, cause)
            }
        }

        fun logE(tag: String?, message: String?) {
            if (BuildConfig.DEBUG && message != null) {
                Log.e(makeLogTag(tag), message)
            }
        }

        fun logE(tag: String?, ex: Exception?) {
            if (BuildConfig.DEBUG && ex != null) {
                Log.e(makeLogTag(tag), Log.getStackTraceString(ex))
            }
        }

        fun logE(tag: String?, ex: Throwable?) {
            if (BuildConfig.DEBUG && ex != null) {
                Log.e(makeLogTag(tag), Log.getStackTraceString(ex))
            }
        }

        fun logE(
            tag: String?, message: String?, cause: Throwable?
        ) {
            if (BuildConfig.DEBUG && message != null && cause != null) {
                Log.e(makeLogTag(tag), message, cause)
            }
        }

        fun logLarge(tag: String?, message: String?) {
            if (BuildConfig.DEBUG && message != null) {
                val maxLogSize = 1000
                for (i in 0..message.length / maxLogSize) {
                    val start = i * maxLogSize
                    var end = (i + 1) * maxLogSize
                    end = if (end > message.length) message.length else end
                    Log.v(makeLogTag(tag), message.substring(start, end))
                }
            }
        }
    }
}