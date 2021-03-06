package com.like.paging.sample.data.db

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

interface BaseDao<T> {
    @Insert
    suspend fun insert(vararg objects: T)

    @Update
    suspend fun update(vararg objects: T)

    @Delete
    suspend fun delete(vararg objects: T)
}