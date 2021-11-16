package com.mvvm.locationmanager

import android.location.Location

interface LocationChangeListener {
    fun onLocationChanged(location: Location?)
}
