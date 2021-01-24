package com.like.paging.sample.paging.dataSource.inDb

import android.content.Context
import android.util.Log
import com.like.common.util.isInternetAvailable
import com.like.paging.sample.data.db.BannerEntityDao
import com.like.paging.sample.data.model.BannerInfo
import com.like.paging.sample.data.netWork.RetrofitUtils

class BannerDbDataSource(private val context: Context, private val bannerEntityDao: BannerEntityDao) {
    private val TAG = BannerDbDataSource::class.java.simpleName

    suspend fun load(isRefresh: Boolean = false): List<BannerInfo>? {
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

    private suspend fun loadFromDb(isRefresh: Boolean): List<BannerInfo>? {
        val data = bannerEntityDao.getAll()
        if (data.isEmpty()) {
            return null
        }
        val bannerInfo = BannerInfo().apply {
            bannerEntities = data
        }
        return listOf(bannerInfo)
    }

    private fun shouldFetch(isRefresh: Boolean, resultType: List<BannerInfo>?): Boolean {
        return context.isInternetAvailable() && (resultType.isNullOrEmpty() || isRefresh)
    }

    private suspend fun fetchFromNetworkAndSaveToDb(isRefresh: Boolean) {
        val data = RetrofitUtils.retrofitApi.getBanner().getDataIfSuccess()
        if (!data.isNullOrEmpty()) {
            if (isRefresh) {
                bannerEntityDao.deleteAll()
            }
            bannerEntityDao.insertAll(data)
        }
    }
}