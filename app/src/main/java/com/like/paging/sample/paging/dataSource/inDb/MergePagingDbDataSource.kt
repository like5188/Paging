package com.like.paging.sample.paging.dataSource.inDb

import com.like.common.util.successIfOneSuccess
import com.like.paging.RequestType
import com.like.paging.dataSource.byPageNoKeyed.PageNoKeyedPagingDataSource
import com.like.paging.sample.MyApplication

class MergePagingDbDataSource(
    private val dbBannerNotPagingDataSource: BannerDbDataSource,
    private val dbTopArticleNotPagingDataSource: TopArticleDbDataSource,
    private val dbArticlePagingDataSource: ArticlePagingDbDataSource,
) : PageNoKeyedPagingDataSource<List<Any>?>(MyApplication.PAGE_SIZE) {

    override fun getInitialPage(): Int {
        return 0
    }

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
                        result.addAll(it)
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