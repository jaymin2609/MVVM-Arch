package com.mvvm.extensions

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat


fun AppCompatActivity.hideToolbar() {
    supportActionBar?.hide()
}

fun AppCompatActivity.showToolbar() {
    supportActionBar?.show()
}

/**
 * Sets color to toolbar in AppCompatActivity
 */
fun AppCompatActivity.setToolbarColor(@ColorRes color: Int) {
    this.supportActionBar?.setBackgroundDrawable(
        ColorDrawable(
            ContextCompat.getColor(this, color)
        )
    )
}

/**
 * Hide Keyboard
 */
fun AppCompatActivity.hideKeyboard() {
    val view = this.currentFocus
    view?.let {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.let { imm.hideSoftInputFromWindow(view.windowToken, 0) }
    }
}