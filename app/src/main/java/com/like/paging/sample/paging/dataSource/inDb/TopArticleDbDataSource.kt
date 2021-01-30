package com.like.paging.sample.paging.dataSource.inDb

import android.content.Context
import com.like.common.util.isInternetAvailable
import com.like.paging.dbHelper.IDbHelper
import com.like.paging.sample.data.db.TopArticleEntityDao
import com.like.paging.sample.data.model.TopArticleEntity
import com.like.paging.sample.data.netWork.RetrofitUtils

class TopArticleDbDataSource(private val context: Context, private val topArticleEntityDao: TopArticleEntityDao) {
    private val mDbHelper = object : IDbHelper<List<TopArticleEntity>?> {
        override suspend fun loadFromDb(isRefresh: Boolean): List<TopArticleEntity>? {
            return topArticleEntityDao.getAll()
        }

        override fun shouldFetch(isRefresh: Boolean, result: List<TopArticleEntity>?): Boolean {
            return context.isInternetAvailable() && (result.isNullOrEmpty() || isRefresh)
        }

        override suspend fun fetchFromNetworkAndSaveToDb(isRefresh: Boolean) {
            val data = RetrofitUtils.retrofitApi.getTopArticle().getDataIfSuccess()
            if (!data.isNullOrEmpty()) {
                if (isRefresh) {
                    topArticleEntityDao.deleteAll()
                }
                topArticleEntityDao.insertAll(data)
            }
        }
    }

    suspend fun load(isRefresh: Boolean = false): List<TopArticleEntity>? {
        return mDbHelper.load(isRefresh)
    }

}