package com.sunteckindia.di

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import com.sunteckindia.locationmanager.LocationChangeListener
import com.sunteckindia.locationmanager.LocationFinderAware
import com.sunteckindia.networking.ApiResponseCallBack
import com.sunteckindia.networking.ApiServiceProviderGeneric2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ActivityComponent::class)
object UserListModule {

    @Provides
    fun provideCallback(activity: Activity) =
        activity as LocationChangeListener

    @Provides
    @ActivityScoped
    fun provideLocationFinderAware(
        @ActivityContext mContext: Context,
        locationChangeListener: LocationChangeListener
    ) = LocationFinderAware(mContext, locationChangeListener)

}