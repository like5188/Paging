package com.like.paging.sample.paging.dataSource.inMemory

import com.like.common.util.successIfOneSuccess
import com.like.paging.RequestType
import com.like.paging.dataSource.byPageNoKeyed.PageNoKeyedPagingDataSource
import com.like.paging.sample.MyApplication

class MergePagingDataSource(
    private val memoryBannerNotPagingDataSource: BannerDataSource,
    private val memoryTopArticleNotPagingDataSource: TopArticleDataSource,
    private val memoryArticlePagingDataSource: ArticlePagingDataSource
) : PageNoKeyedPagingDataSource<List<Any>?>(MyApplication.PAGE_SIZE) {

    override suspend fun load(requestType: RequestType, pageNo: Int, pageSize: Int): List<Any>? {
        return when (requestType) {
            RequestType.Initial, RequestType.Refresh -> {
                val result = mutableListOf<Any>()
                successIfOneSuccess(
                    { memoryBannerNotPagingDataSource.load() },
                    { memoryTopArticleNotPagingDataSource.load() },
                    { memoryArticlePagingDataSource.load(requestType, pageNo, pageSize) }
                ).forEach {
                    it?.let {
                        result.addAll(it as List<Any>)
                    }
                }
                result
            }
            else -> {
                memoryArticlePagingDataSource.load(requestType, pageNo, pageSize)
            }
        }
    }

    override fun getInitialPage(): Int {
        return 0
    }
}