package com.like.paging.byPageNoKeyed

import androidx.annotation.WorkerThread
import com.like.paging.RequestType
import com.like.paging.PagingDataSource

/**
 * 根据自己维护的 pageNo 来作为分页 key 的分页数据源。
 */
abstract class PageNoKeyedPagingDataSource<ResultType>(
    private val pageSize: Int,
    isLoadAfter: Boolean = true
) : PagingDataSource<ResultType>(isLoadAfter) {
    private var pageNo: Int = 1

    final override suspend fun load(requestType: RequestType): ResultType {
        when (requestType) {
            is RequestType.Initial, is RequestType.Refresh -> {
                pageNo = getInitialPage()
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
            when (requestType) {
                is RequestType.Before -> {
                    pageNo++
                }
                is RequestType.After -> {
                    pageNo--
                }
            }
            throw e
        }
    }

    /**
     * 获取初始页码。默认为1
     */
    protected open fun getInitialPage(): Int = 1

    /**
     * @param requestType   请求类型：[RequestType]
     * @param pageNo        页码
     * @param pageSize      每页加载数量
     */
    @WorkerThread
    abstract suspend fun load(requestType: RequestType, pageNo: Int, pageSize: Int): ResultType

}