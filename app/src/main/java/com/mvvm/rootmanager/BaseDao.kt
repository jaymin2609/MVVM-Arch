package com.mvvm.rootmanager

import androidx.room.*


@Dao
interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(obj: List<T>): List<Long>

    @Update
    suspend fun update(obj: T): Int

    @Update
    suspend fun updateAll(objList: List<T>)

    @Delete
    suspend fun delete(obj: T)
}
