package com.mvvm.di

import android.app.Activity
import android.content.Context
import com.mvvm.locationmanager.LocationChangeListener
import com.mvvm.locationmanager.LocationFinderAware
import com.mvvm.utilities.ProgressBar
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

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

    @Provides
    @ActivityScoped
    fun provideProgressBar(@ActivityContext mContext: Context) = ProgressBar(
        context = mContext
    )

}