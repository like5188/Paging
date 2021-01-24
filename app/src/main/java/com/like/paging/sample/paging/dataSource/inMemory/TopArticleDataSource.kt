package com.like.paging.sample.paging.dataSource.inMemory

import com.like.paging.sample.data.model.TopArticleEntity
import com.like.paging.sample.data.netWork.RetrofitUtils

class TopArticleDataSource {

    suspend fun load(): List<TopArticleEntity>? {
        return RetrofitUtils.retrofitApi.getTopArticle().getDataIfSuccess()
    }

}