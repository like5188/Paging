package com.like.paging.dataSource

import com.like.paging.RequestType
import com.like.paging.Result
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * 分页数据源基类
 * 功能：
 * 1、管理初始化、刷新、往后加载更多、往前加载更多、重试操作。
 * 2、管理请求状态。[RequestState]
 * 3、限制请求。如果当前正在执行，则不管新请求。
 * 注意：mFlow.collect() 过后，不会取消，会一直搜集数据。
 *
 * @param ResultType    返回的数据类型
 * @param isLoadAfter   true：往后加载更多（默认值）；false：往前加载更多。
 */
abstract class PagingDataSource<ResultType>(private val isLoadAfter: Boolean) {
    private fun initial() = loadData(RequestType.Initial)
    private fun refresh() = loadData(RequestType.Refresh)
    private fun loadAfter() = loadData(RequestType.After)
    private fun loadBefore() = loadData(RequestType.Before)
    private fun loadData(requestType: RequestType) = flow {
        val data = withContext(Dispatchers.IO) {
            this@PagingDataSource.load(requestType)
        }
        emit(data)
    }

    fun result(): Result<ResultType> = Result(
        initial = this::initial,
        refresh = this::refresh,
        loadAfter = if (isLoadAfter) this::loadAfter else null,
        loadBefore = if (isLoadAfter) null else this::loadBefore
    )

    abstract suspend fun load(requestType: RequestType): ResultType

}
