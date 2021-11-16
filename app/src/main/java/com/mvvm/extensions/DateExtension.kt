package com.mvvm.extensions

import com.mvvm.utilities.LogUtils
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Convert a given date to milliseconds
 */
fun Date.toMillis(): Long {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.timeInMillis
}

/**
 * Checks if dates are same
 */
fun Date.isSame(to: Date): Boolean {
    val sdf = SimpleDateFormat("yyyMMdd", Locale.getDefault())
    return sdf.format(this) == sdf.format(to)
}

/**
 * Converts raw string to date object using [SimpleDateFormat]
 */
fun String.convertStringToDate(simpleDateFormatPattern: String): Date? {
    var value: Date? = null
    justTry {
        val simpleDateFormat = SimpleDateFormat(simpleDateFormatPattern, Locale.US)
        value = simpleDateFormat.parse(this)
        return value
    }
    return value
}

fun getCurrentDateTime(formate: String): String {
    return try {
        val dateFormatter: DateFormat = SimpleDateFormat(formate, Locale.getDefault())
        dateFormatter.isLenient = false
        val today = Date()
        dateFormatter.format(today)
    } catch (e: java.lang.Exception) {
        LogUtils.logE("TAG", e)
        ""
    }
}
