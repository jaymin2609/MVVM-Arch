package com.mvvm.prefmanagers

import android.content.Context
import android.content.SharedPreferences
import com.mvvm.utilities.PREF_DEMO_APP

class Pref {
    companion object {
        private const val PREF_FILE = PREF_DEMO_APP
        private lateinit var sharedPreferences: SharedPreferences

        private fun openPref(context: Context) {
            sharedPreferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)
        }

        fun setValue(context: Context, key: String, value: String) {
            openPref(context)
            val prefsPrivateEditor: SharedPreferences.Editor? = sharedPreferences.edit()
            prefsPrivateEditor!!.putString(key, value)
            prefsPrivateEditor.apply()
        }

        fun getValue(context: Context, key: String, defaultValue: String): String {
            openPref(context)
            val result = sharedPreferences.getString(key, defaultValue)
            return result!!
        }
    }

}