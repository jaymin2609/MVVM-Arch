package com.mvvm.rootmanager

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mvvm.BuildConfig
import com.mvvm.database.dao.UserDao
import com.mvvm.database.entity.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var appDatabase: AppDatabase? = null
        private var databaseName = "Sunteck_India_DB"


        fun getInstance(context: Context): AppDatabase {
            val tempInstance = appDatabase
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                databaseName += if (BuildConfig.FLAVOR.equals("qa", ignoreCase = false)) {
                    "_QA.db"
                } else {
                    "_PROD.db"
                }
                val instance = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, databaseName
                )
                    .build()
                appDatabase = instance
                return instance
            }
        }
    }
}