package com.sunteckindia.database.repository

import com.sunteckindia.database.dao.UserDao
import com.sunteckindia.database.entity.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class UserRepository @Inject constructor(@Named("userDao") val userDao: UserDao) {
    fun getUserData() = userDao.getUserData()


    suspend fun insertAll(list: List<UserEntity>): List<Long> {
        return withContext(Dispatchers.IO) {
            userDao.insertAll(list)
        }
    }

    suspend fun insert(userEntity: UserEntity): Long {
        return withContext(Dispatchers.IO) {
            userDao.insert(userEntity)
        }
    }

    fun getUserDataNonLive(
        name: String
    ): UserEntity? {
        return userDao.getUserDataNonLive("%$name%")
    }
}