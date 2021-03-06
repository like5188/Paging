package com.like.paging.sample.paging.dataSource.inMemory

import com.like.paging.RequestType
import com.like.paging.dataSource.byPageNoKeyed.PageNoKeyedPagingDataSource
import com.like.paging.sample.MyApplication
import com.like.paging.sample.data.model.ArticleEntity
import com.like.paging.sample.data.netWork.RetrofitUtils

class ArticlePagingDataSource : PageNoKeyedPagingDataSource<List<ArticleEntity>?>(MyApplication.PAGE_SIZE) {

    override suspend fun load(requestType: RequestType, pageNo: Int, pageSize: Int): List<ArticleEntity>? {
        return RetrofitUtils.retrofitApi.getArticle(pageNo).getDataIfSuccess()?.datas
    }

    override fun getInitialPage(): Int {
        return 0
    }

}