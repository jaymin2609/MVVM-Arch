package com.sunteckindia.utilities

import android.content.Context
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDialog

object AlertDialogManager {
    fun getCustomDialogWithTransparentBack(context: Context, layoutId: Int): AppCompatDialog {
        val appCompatDialog = AppCompatDialog(context)
        appCompatDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        appCompatDialog.setContentView(layoutId)
        appCompatDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        appCompatDialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

        return appCompatDialog
    }
}