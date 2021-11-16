package com.like.paging.dataSource

import com.like.paging.RequestType
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
 */
abstract class PagingDataSource<ResultType> {

    open fun initial(): Flow<ResultType> = loadData(RequestType.Initial)
    open fun refresh(): Flow<ResultType> = loadData(RequestType.Refresh)
    open fun loadAfter(): Flow<ResultType> = loadData(RequestType.After)
    open fun loadBefore(): Flow<ResultType> = loadData(RequestType.Before)

    private fun loadData(requestType: RequestType) = flow {
        val data = withContext(Dispatchers.IO) {
            this@PagingDataSource.load(requestType)
        }
        emit(data)
    }

    abstract suspend fun load(requestType: RequestType): ResultType

}
