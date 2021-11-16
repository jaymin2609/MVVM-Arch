package com.mvvm.rootmanager

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import com.mvvm.extensions.makeGone
import com.mvvm.utilities.Utils


object DataBindingAdapters {

    @JvmStatic
    @BindingAdapter("intToStr")
    fun AppCompatTextView.intToStr(data: Int) {
        if (data <= 0) {
            makeGone()
        }
        text = "${data}"
    }

    @JvmStatic
    @BindingAdapter("errorTextInputLayout")
    fun TextInputLayout.errorTextInputLayout(data: String) {
        error = data
        if (!Utils.isStringNull(data)) {
            requestFocus()
        }

        editText!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                error = null
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })
    }

    @JvmStatic
    @BindingAdapter("errorAppCompatEditText")
    fun AppCompatEditText.errorAppCompatEditText(data: String) {
        error = data
    }

    @JvmStatic
    @BindingAdapter("manageErrorVisibility")
    fun View.manageErrorVisibility(status: Status) {
        if (status == Status.ERROR) {
            visibility = View.VISIBLE
        } else {
            visibility = View.GONE
        }
    }

    @JvmStatic
    @BindingAdapter("manageSuccessVisibility")
    fun View.manageSuccessVisibility(status: Status) {
        if (status == Status.SUCCESS) {
            visibility = View.VISIBLE
        } else {
            visibility = View.GONE

        }
    }

    @JvmStatic
    @BindingAdapter("manageLoadingVisibility")
    fun View.manageLoadingVisibility(status: Status) {
        if (status == Status.LOADING) {
            visibility = View.VISIBLE
        } else {
            visibility = View.GONE

        }
    }
}
