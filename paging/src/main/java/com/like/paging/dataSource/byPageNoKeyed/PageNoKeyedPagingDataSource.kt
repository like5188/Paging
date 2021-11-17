package com.like.paging.dataSource.byPageNoKeyed

import com.like.paging.RequestType
import com.like.paging.dataSource.PagingDataSource

/**
 * 根据自己维护的 pageNo 来作为分页 key 的分页数据源。
 *
 * @param firstPage     初始页码。默认为 1
 * @param lastPage      最后页码。默认为 [Int.MAX_VALUE]
 * @param pageSize      每页加载数量。默认为 10
 */
abstract class PageNoKeyedPagingDataSource<ResultType>(
    private val firstPage: Int = 1,
    private val lastPage: Int = Int.MAX_VALUE,
    private val pageSize: Int = 10
) : PagingDataSource<ResultType>() {
    private var pageNo: Int = firstPage

    final override suspend fun load(requestType: RequestType): ResultType {
        when (requestType) {
            is RequestType.Initial, is RequestType.Refresh -> {
                pageNo = firstPage
            }
            is RequestType.Before -> {
                pageNo--
                if (pageNo < firstPage) {
                    pageNo = firstPage
                }
            }
            is RequestType.After -> {
                pageNo++
                if (pageNo > lastPage) {
                    pageNo = lastPage
                }
            }
        }
        return try {
            load(requestType, pageNo, pageSize)
        } catch (e: Exception) {
            // 还原 pageNo
            when (requestType) {
                is RequestType.Before -> {
                    pageNo++
                    if (pageNo > lastPage) {
                        pageNo = lastPage
                    }
                }
                is RequestType.After -> {
                    pageNo--
                    if (pageNo < firstPage) {
                        pageNo = firstPage
                    }
                }
            }
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