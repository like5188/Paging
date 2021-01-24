package com.like.paging.sample.paging.dataSource.inDb

import android.content.Context
import android.util.Log
import com.like.common.util.isInternetAvailable
import com.like.paging.sample.data.db.TopArticleEntityDao
import com.like.paging.sample.data.model.TopArticleEntity
import com.like.paging.sample.data.netWork.RetrofitUtils

class TopArticleDbDataSource(private val context: Context, private val topArticleEntityDao: TopArticleEntityDao)  {
    private val TAG = BannerDbDataSource::class.java.simpleName

    suspend fun load(isRefresh: Boolean = false): List<TopArticleEntity>? {
        var data = loadFromDb(isRefresh)
        if (shouldFetch(isRefresh, data)) {
            Log.d(TAG, "即将从网络获取数据并存入数据库中")
            fetchFromNetworkAndSaveToDb(isRefresh)
            Log.d(TAG, "即将重新从数据库获取数据")
            data = loadFromDb(isRefresh)
        }
        Log.d(TAG, "从数据库获取到了数据：$data")
        return data
    }

    private suspend fun loadFromDb(isRefresh: Boolean): List<TopArticleEntity>? {
        return topArticleEntityDao.getAll()
    }

    private fun shouldFetch(isRefresh: Boolean, resultType: List<TopArticleEntity>?): Boolean {
        return context.isInternetAvailable() && (resultType.isNullOrEmpty() || isRefresh)
    }

    private suspend fun fetchFromNetworkAndSaveToDb(isRefresh: Boolean) {
        val data = RetrofitUtils.retrofitApi.getTopArticle().getDataIfSuccess()
        if (!data.isNullOrEmpty()) {
            if (isRefresh) {
                topArticleEntityDao.deleteAll()
            }
            topArticleEntityDao.insertAll(data)
        }
    }

}