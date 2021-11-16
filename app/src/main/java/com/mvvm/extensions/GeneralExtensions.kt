package com.mvvm.extensions

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.mvvm.BuildConfig
import com.mvvm.rootmanager.BaseViewModelFactory
import com.mvvm.utilities.LogUtils
import java.util.*


/**
 * Wrapping try/catch to ignore catch block
 */
inline fun <T> justTry(block: () -> T) = try {
    block()
} catch (e: Exception) {
    LogUtils.logE("GeneralExtension", e.toString())
}

/**
 * App's debug mode
 */
inline fun debugMode(block: () -> Unit) {
    if (BuildConfig.DEBUG) {
        block()
    }
}

inline fun <reified T : ViewModel> Fragment.getViewModel(noinline creator: (() -> T)? = null): T {
    return if (creator == null)
        ViewModelProvider(this).get(T::class.java)
    else
        ViewModelProvider(this, BaseViewModelFactory(creator)).get(T::class.java)
}

inline fun <reified T : ViewModel> AppCompatActivity.getViewModel(noinline creator: (() -> T)? = null): T {
    return if (creator == null)
        ViewModelProvider(this).get(T::class.java)
    else
        ViewModelProvider(this, BaseViewModelFactory(creator)).get(T::class.java)
}

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}

fun Uri.getRealPathString(context: Context): String {
    var path = ""
    justTry {
        context.contentResolver.query(this, arrayOf(MediaStore.Images.Media.DATA), null, null, null)
            ?.apply {
                val columnIndex = getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                moveToFirst()
                path = getString(columnIndex)
                close()
            }
    }
    return path
}

fun String.getUri(): Uri {
    return Uri.parse(this)
}

fun String.getPathFromStringUri(): String {
    return Uri.parse(this).path.toString()
}

fun String.toDoubleDigit(): String {
    return String.format("%02d", this.toInt())
}

fun String.toCapFirstLetter(): String {
    return if (this.isNotEmpty()) this.substring(
        0, 1
    ).toUpperCase(Locale.ROOT) + this.substring(1).toLowerCase(Locale.ROOT) else ""
}

fun String.toReplaceDotWithSpace(): String {
    return this.replace(".", " ")
}

