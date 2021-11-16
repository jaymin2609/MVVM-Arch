package com.sunteckindia.rootmanager

import android.Manifest
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.snackbar.Snackbar
import com.sunteckindia.extensions.snack

open class BaseActivity : AppCompatActivity() {


    fun showSnackBar(message: String) {
        this.findViewById<View>(android.R.id.content).snack(message) {}
    }

    fun showLongSnackBar(message: String) {
        this.findViewById<View>(android.R.id.content).snack(message, Snackbar.LENGTH_LONG) {}
    }

    fun getStoragePermission(): MutableList<String> {
        return arrayListOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    fun setToolbar(toolbar: Toolbar, title: String) {
        toolbar.title = title
        setSupportActionBar(toolbar)
    }

    fun setToolbarWithHome(toolbar: Toolbar, title: String) {
        toolbar.title = title
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }
    }

    fun getPhoneStatePermission(): Array<String?>? {
        return arrayOf(Manifest.permission.READ_PHONE_STATE)
    }

    fun getLocationPermission(): MutableList<String> {
        return arrayListOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    fun getLocationAndPhonePermission(): Array<String?>? {
        return arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE
        )
    }

}