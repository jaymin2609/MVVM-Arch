package com.mvvm.rootmanager

import android.content.Context
import com.mvvm.Application

object ResourceProvider {

    val applicationContext: Context by lazy {
        Application.instance.applicationContext
    }

}