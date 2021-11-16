package com.sunteckindia.rootmanager

import android.content.Context
import com.google.gson.reflect.TypeToken
import com.sunteckindia.Application
import java.lang.reflect.Type

object ResourceProvider {

    val applicationContext: Context by lazy {
        Application.instance.applicationContext
    }

}