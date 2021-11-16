package com.mvvm.rootmanager

import android.content.Context
import com.mvvm.database.dao.UserDao

object Injector {

    private fun getDBInstance(context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    fun userDao(context: Context): UserDao {
        return getDBInstance(context).userDao()
    }


}
