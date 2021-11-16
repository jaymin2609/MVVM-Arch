package com.mvvm.locationmanager

import com.google.android.gms.location.LocationSettingsResponse

interface GPSResponder {
    fun onSuccess(locationSettingsResponse: LocationSettingsResponse?)
    fun onFailure(e: Exception)
}