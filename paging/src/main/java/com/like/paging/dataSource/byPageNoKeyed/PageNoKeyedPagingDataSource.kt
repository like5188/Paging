package com.like.paging.dataSource.byPageNoKeyed

import com.like.paging.RequestType
import com.like.paging.dataSource.PagingDataSource

/**
 * 根据自己维护的 pageNo 来作为分页 key 的分页数据源。
 *
 * @param initialPage       往前或者往后的初始页码。默认为 1
 * @param pageSize          每页加载数量。默认为 10
 */
abstract class PageNoKeyedPagingDataSource<ResultType>(
    private val initialPage: Int = 1,
    private val pageSize: Int = 10
) : PagingDataSource<ResultType>() {
    private var pageNo: Int = initialPage

    final override suspend fun load(requestType: RequestType): ResultType {
        val prePageNo = pageNo
        when (requestType) {
            is RequestType.Initial, is RequestType.Refresh -> {
                pageNo = initialPage
            }
            is RequestType.Before -> {
                pageNo--
            }
            is RequestType.After -> {
                pageNo++
            }
        }
        return try {
            load(requestType, pageNo, pageSize)
        } catch (e: Exception) {
            // 还原 pageNo
            pageNo = prePageNo
            throw e
        }
    }

    /**
     * @param requestType   请求类型：[RequestType]
     * @param pageNo        页码
     * @param pageSize      每页加载数量
     */
    abstract suspend fun load(requestType: RequestType, pageNo: Int, pageSize: Int): ResultType

}