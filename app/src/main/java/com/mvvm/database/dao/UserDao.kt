package com.mvvm.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.mvvm.database.entity.UserEntity
import com.mvvm.rootmanager.BaseDao
import com.mvvm.utilities.DBConstant.TB_COL_NAME
import com.mvvm.utilities.DBConstant.TB_USERS

@Dao
interface UserDao : BaseDao<UserEntity> {

    @Query("SELECT * FROM $TB_USERS")
    fun getUserData(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM $TB_USERS where $TB_COL_NAME  LIKE:name")
    fun getUserDataNonLive(name: String): UserEntity?


}