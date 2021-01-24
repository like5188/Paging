package com.like.paging.sample.paging.dataSource.inMemory

import com.like.paging.sample.data.model.BannerInfo
import com.like.paging.sample.data.netWork.RetrofitUtils

class BannerDataSource {

    suspend fun load(): List<BannerInfo>? {
        val result = RetrofitUtils.retrofitApi.getBanner().getDataIfSuccess()
        return if (result.isNullOrEmpty()) {
            null
        } else {
            val bannerInfo = BannerInfo().apply {
                bannerEntities = result
            }
            listOf(bannerInfo)
        }
    }

}