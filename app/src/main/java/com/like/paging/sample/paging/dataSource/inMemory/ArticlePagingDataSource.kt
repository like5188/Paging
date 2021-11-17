package com.like.paging.sample.paging.dataSource.inMemory

import com.like.common.util.Logger
import com.like.paging.RequestType
import com.like.paging.dataSource.byPageNoKeyed.PageNoKeyedPagingDataSource
import com.like.paging.sample.data.model.ArticleEntity
import com.like.paging.sample.data.netWork.RetrofitUtils

class ArticlePagingDataSource : PageNoKeyedPagingDataSource<List<ArticleEntity>?>(0) {
    private var i = 0

    override suspend fun load(requestType: RequestType, pageNo: Int, pageSize: Int): List<ArticleEntity>? {
        Logger.d("load requestType=$requestType pageNo=$pageNo pageSize=$pageSize")
        when (i++) {
            0 -> throw RuntimeException("test error 0")
            1 -> throw RuntimeException("test error 1")
            2 -> throw RuntimeException("test error 2")
            else -> return RetrofitUtils.retrofitApi.getArticle(pageNo).getDataIfSuccess()?.datas
        }
    }

}