package com.sunteckindia.utilities

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.widget.AppCompatTextView
import com.sunteckindia.R

class ProgressBar(var title: String="", isCancelable: Boolean=false, val context: Context) {
    private val classTag = ProgressBar::class.java.simpleName
    private var dialog: AppCompatDialog =
        AlertDialogManager.getCustomDialogWithTransparentBack(context, R.layout.dialog_loading)

    init {
        dialog.setCancelable(isCancelable)

        if (title.isEmpty()) {
            title = context.getString(R.string.msg_please_wait)
        }
        val tvTitle = dialog.findViewById<AppCompatTextView>(R.id.tvTitle)
        tvTitle?.text = title
    }

    fun showLoader() {
        try {
            if (!dialog.isShowing && !(context as Activity).isDestroyed && !context.isFinishing) {
                dialog.show()
                LogUtils.logE(classTag, "dataLoading : showLoader : ")
            }
        } catch (e: Exception) {
            LogUtils.logE(classTag, e)
        }
    }

    fun hideLoader() {
        try {
            if (dialog.isShowing) {
                dialog.dismiss()
                LogUtils.logE(classTag, "dataLoading : hideLoader : ")
            }
        } catch (e: Exception) {
            LogUtils.logE(classTag, e)
        }
    }


}