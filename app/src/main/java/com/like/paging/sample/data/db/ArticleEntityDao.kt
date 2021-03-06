package com.like.paging.sample.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.like.paging.sample.data.model.ArticleEntity

@Dao
interface ArticleEntityDao : BaseDao<ArticleEntity> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ArticleEntity>)

    @Query("DELETE FROM ArticleEntity")
    suspend fun deleteAll()

    @Query("SELECT * FROM ArticleEntity ORDER BY id ASC")
    suspend fun getAll(): List<ArticleEntity>

    @Query("SELECT * FROM ArticleEntity ORDER BY id ASC limit :pageSize offset :offset")
    suspend fun getPage(offset: Int, pageSize: Int): List<ArticleEntity>
}