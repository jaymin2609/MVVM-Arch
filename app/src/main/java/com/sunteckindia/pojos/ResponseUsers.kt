package com.sunteckindia.pojos

import com.sunteckindia.database.entity.UserEntity

data class ResponseUsers(
    val userList: List<UserEntity>
)