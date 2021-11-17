package com.like.paging.dataSource.byPageNoKeyed

import com.like.paging.RequestType
import com.like.paging.dataSource.PagingDataSource

/**
 * 根据自己维护的 pageNo 来作为分页 key 的分页数据源。
 *
 * @param initPage      往前或者往后的初始页码。默认为 1
 * @param limitPage     往前或者往后的极限页码。默认为 [Int.MAX_VALUE]
 * @param pageSize      每页加载数量。默认为 10
 */
abstract class PageNoKeyedPagingDataSource<ResultType>(
    private val initPage: Int = 1,
    private val limitPage: Int = Int.MAX_VALUE,
    private val pageSize: Int = 10
) : PagingDataSource<ResultType>() {
    private var pageNo: Int = initPage

    final override suspend fun load(requestType: RequestType): ResultType {
        when (requestType) {
            is RequestType.Initial, is RequestType.Refresh -> {
                pageNo = initPage
            }
            is RequestType.Before -> {
                pageNo--
                if (pageNo < limitPage) {
                    pageNo = limitPage
                }
            }
            is RequestType.After -> {
                pageNo++
                if (pageNo > limitPage) {
                    pageNo = limitPage
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
                    if (pageNo > initPage) {
                        pageNo = initPage
                    }
                }
                is RequestType.After -> {
                    pageNo--
                    if (pageNo < initPage) {
                        pageNo = initPage
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