package com.sunteckindia.locationmanager

import android.location.Location

interface LocationChangeListener {
    fun onLocationChanged(location: Location?)
}
