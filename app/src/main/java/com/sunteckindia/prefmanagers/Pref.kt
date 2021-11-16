package com.sunteckindia.prefmanagers

import android.content.Context
import android.content.SharedPreferences
import com.sunteckindia.utilities.PREF_SUNTECK_INDIA

class Pref {
    companion object {
        private const val PREF_FILE = PREF_SUNTECK_INDIA
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