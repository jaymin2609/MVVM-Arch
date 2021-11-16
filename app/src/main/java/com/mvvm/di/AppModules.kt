package com.mvvm.di

import android.content.Context
import com.mvvm.database.dao.UserDao
import com.mvvm.rootmanager.AppDatabase
import com.mvvm.utilities.CONNECTION_TIMEOUT
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModules {

    @Provides
    @Singleton
    fun getOkHttpClient(): OkHttpClient.Builder {
        return OkHttpClient.Builder().also {
            it.addInterceptor(HttpLoggingInterceptor())
            it.readTimeout(CONNECTION_TIMEOUT, TimeUnit.MINUTES)
            it.writeTimeout(CONNECTION_TIMEOUT, TimeUnit.MINUTES)
            it.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.MINUTES)
            it.build()
        }
    }


    /*@Provides
    @Singleton
    fun getDBInstance(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        DATABASE_NAME,
    ).build()*/


    @Provides
    @Singleton
    fun getDBInstance(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    @Named("userDao")
    fun userDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

//    @Provides
//    @Singleton
//    fun provideApiServiceGeneric() = ApiServiceProviderGeneric()


}