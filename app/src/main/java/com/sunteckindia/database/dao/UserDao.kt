package com.sunteckindia.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.sunteckindia.database.entity.UserEntity
import com.sunteckindia.rootmanager.BaseDao
import com.sunteckindia.utilities.DBConstant.TB_COL_NAME
import com.sunteckindia.utilities.DBConstant.TB_USERS

@Dao
interface UserDao : BaseDao<UserEntity> {

    @Query("SELECT * FROM $TB_USERS")
    fun getUserData(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM $TB_USERS where $TB_COL_NAME  LIKE:name")
    fun getUserDataNonLive(name: String): UserEntity?


}