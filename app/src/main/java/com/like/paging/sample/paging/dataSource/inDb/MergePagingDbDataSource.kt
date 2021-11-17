package com.like.paging.sample.paging.dataSource.inDb

import com.like.common.util.successIfOneSuccess
import com.like.paging.RequestType
import com.like.paging.dataSource.byPageNoKeyed.PageNoKeyedPagingDataSource

class MergePagingDbDataSource(
    private val dbBannerNotPagingDataSource: BannerDbDataSource,
    private val dbTopArticleNotPagingDataSource: TopArticleDbDataSource,
    private val dbArticlePagingDataSource: ArticlePagingDbDataSource,
) : PageNoKeyedPagingDataSource<List<Any>?>(0) {

    override suspend fun load(requestType: RequestType, pageNo: Int, pageSize: Int): List<Any>? {
        return when (requestType) {
            RequestType.Initial, RequestType.Refresh -> {
                val result = mutableListOf<Any>()
                successIfOneSuccess(
                    { dbBannerNotPagingDataSource.load(requestType == RequestType.Refresh) },
                    { dbTopArticleNotPagingDataSource.load(requestType == RequestType.Refresh) },
                    { dbArticlePagingDataSource.load(requestType, pageNo, pageSize) }
                ).forEach {
                    it?.let {
                        result.addAll(it as List<Any>)
                    }
                }
                result
            }
            else -> {
                dbArticlePagingDataSource.load(requestType, pageNo, pageSize)
            }
        }
    }

}