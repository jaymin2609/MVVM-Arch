package com.mvvm.pojos

import com.mvvm.database.entity.UserEntity

data class ResponseUsers(
    val userList: List<UserEntity>
)