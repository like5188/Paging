package com.like.paging.sample.paging.dataSource.inDb

import android.content.Context
import com.like.common.util.isInternetAvailable
import com.like.paging.dbHelper.IDbHelper
import com.like.paging.sample.data.db.BannerEntityDao
import com.like.paging.sample.data.model.BannerInfo
import com.like.paging.sample.data.netWork.RetrofitUtils

class BannerDbDataSource(private val context: Context, private val bannerEntityDao: BannerEntityDao) {
    private val mDbHelper = object : IDbHelper<List<BannerInfo>?> {
        override suspend fun loadFromDb(isRefresh: Boolean): List<BannerInfo>? {
            val data = bannerEntityDao.getAll()
            if (data.isEmpty()) {
                return null
            }
            val bannerInfo = BannerInfo().apply {
                bannerEntities = data
            }
            return listOf(bannerInfo)
        }

        override fun shouldFetch(isRefresh: Boolean, result: List<BannerInfo>?): Boolean {
            return context.isInternetAvailable() && (result.isNullOrEmpty() || isRefresh)
        }

        override suspend fun fetchFromNetworkAndSaveToDb(isRefresh: Boolean) {
            val data = RetrofitUtils.retrofitApi.getBanner().getDataIfSuccess()
            if (!data.isNullOrEmpty()) {
                if (isRefresh) {
                    bannerEntityDao.deleteAll()
                }
                bannerEntityDao.insertAll(data)
            }
        }
    }

    suspend fun load(isRefresh: Boolean = false): List<BannerInfo>? {
        return mDbHelper.load(isRefresh)
    }

}