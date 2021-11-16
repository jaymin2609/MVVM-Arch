package com.sunteckindia.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.sunteckindia.utilities.DBConstant.TB_COL_AGE
import com.sunteckindia.utilities.DBConstant.TB_COL_EMAIL
import com.sunteckindia.utilities.DBConstant.TB_COL_NAME
import com.sunteckindia.utilities.DBConstant.TB_USERS

@Entity(
    tableName = TB_USERS
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @SerializedName("name") @ColumnInfo(name = TB_COL_NAME) val name: String = "",
    @ColumnInfo(name = TB_COL_AGE) val age: Int = 0,
    @SerializedName("email") @ColumnInfo(name = TB_COL_EMAIL) val email: String = ""
) {
    constructor() : this(0, "", 0, "")
//    constructor(id: Int=0,name: String="",age: Int=0,email: String="") : this(id, name, age, email)
}