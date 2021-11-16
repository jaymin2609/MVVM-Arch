package com.mvvm.locationmanager

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.mvvm.utilities.LogUtils
import com.mvvm.utilities.REQUEST_CHECK_SETTINGS
import javax.inject.Inject

class LocationFinderAware @Inject constructor(
    private val mContext: Context,
    private val locationChangeListener: LocationChangeListener
) : LocationCallback(), LocationListener, LifecycleObserver {
    private val classTag = LocationFinderAware::class.java.simpleName
    private var mLocationRequest: LocationRequest? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var locationSettingsRequest: LocationSettingsRequest? = null
    private val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 7000
    private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2
    var lastKnownLocation: Location? = null
        private set

    init {
        val componentActivity: ComponentActivity = mContext as ComponentActivity
        componentActivity.lifecycle.addObserver(this)
        buildApiClient()
        createLocationRequest()
        buildLocationSettingsRequest()
    }

    override fun onLocationResult(locationResult: LocationResult) {
        super.onLocationResult(locationResult)
        onLocationChanged(locationResult.lastLocation)
    }

    private fun buildApiClient() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext)
    }

    private fun createLocationRequest() {
        // Create the location request to start receiving updates
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest!!.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private fun buildLocationSettingsRequest() {
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        locationSettingsRequest = builder.build()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        LogUtils.logV(classTag, "onResume");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        stopLocationService()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        LogUtils.logV(classTag, "onStart");
    }

    fun checkLocationSettings() {
        val settingsClient = LocationServices.getSettingsClient(mContext)
        val task =
            settingsClient.checkLocationSettings(locationSettingsRequest)
        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        task.addOnSuccessListener(
            (mContext as Activity)
        ) {
            lastLocation
            startLocationService()
        }
        task.addOnFailureListener(mContext) { e ->
            if (e is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    e.startResolutionForResult(
                        mContext,
                        REQUEST_CHECK_SETTINGS
                    )
                } catch (sendEx: SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    fun checkLocationSettings(gpsResponder: GPSResponder) {
        val settingsClient = LocationServices.getSettingsClient(mContext)
        val task =
            settingsClient.checkLocationSettings(locationSettingsRequest)
        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        task.addOnSuccessListener(
            (mContext as Activity)
        ) { locationSettingsResponse ->
            gpsResponder.onSuccess(locationSettingsResponse)
            lastLocation
            startLocationService()
        }
        task.addOnFailureListener(mContext) { e ->
            gpsResponder.onFailure(e)
            if (e is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    e.startResolutionForResult(
                        mContext,
                        REQUEST_CHECK_SETTINGS
                    )
                } catch (sendEx: SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }//aa  LogUtils.d(classTag, "Error trying to get last GPS location");
    //aa   LogUtils.e(classTag, e);
// GPS location can be null if GPS is switched off

    // Get last known recent location using new Google Play Services SDK (v11+)
    private val lastLocation: Unit
        private get() {
            // Get last known recent location using new Google Play Services SDK (v11+)
            if (ActivityCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            fusedLocationProviderClient!!.lastLocation
                .addOnSuccessListener { location -> // GPS location can be null if GPS is switched off
                    location?.let { onLocationChanged(it) }
                }
                .addOnFailureListener {
                    LogUtils.logV(classTag, "Error trying to get last GPS location")
                    LogUtils.logE(classTag, it)
                }
        }

    private fun startLocationService() {
        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient!!.requestLocationUpdates(
            mLocationRequest,
            this,
            Looper.myLooper()
        )
    }

    private fun stopLocationService() {
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        if (fusedLocationProviderClient != null) {
            fusedLocationProviderClient!!.removeLocationUpdates(this)
        }
    }

    override fun onLocationChanged(location: Location) {
        lastKnownLocation = location
        LogUtils.logV(
            classTag,
            " lat : " + location.latitude + " lng : " + location.longitude
        )
        locationChangeListener?.onLocationChanged(location)
    }
}