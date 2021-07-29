package com.like.paging.dataSource

import com.like.paging.RequestState
import com.like.paging.RequestType
import com.like.paging.Result
import com.like.paging.ResultReport
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 分页数据源基类
 * 功能：
 * 1、管理请求状态。[RequestState]
 * 2、限制请求。如果当前正在执行，则不管新请求。
 * 注意：getResultReportFlow() 得到的 [Flow] collect 过后，不会取消，会一直搜集数据。
 *
 * @param ResultType    返回的数据类型
 * @param isLoadAfter   true：往后加载更多（默认值）；false：往前加载更多。
 */
abstract class PagingDataSource<ResultType>(private val isLoadAfter: Boolean) {
    private val isRunning = AtomicBoolean(false)
    private var mFailureRequestType: RequestType? = null
    private val mFlow = MutableSharedFlow<ResultReport<ResultType>>()

    fun isRunning() = isRunning.get()

    private fun getResultReportFlow(): Flow<ResultReport<ResultType>> = mFlow

    private suspend fun initial() {
        this.loadData(RequestType.Initial)
    }

    private suspend fun refresh() {
        this.loadData(RequestType.Refresh)
    }

    private suspend fun loadAfter() {
        this.loadData(RequestType.After)
    }

    private suspend fun loadBefore() {
        this.loadData(RequestType.Before)
    }

    private suspend fun retry() {
        mFailureRequestType?.let {
            this.loadData(it)
        }
    }

    private suspend fun loadData(requestType: RequestType) {
        if (isRunning.compareAndSet(false, true)) {
            mFlow.emit(ResultReport(requestType, RequestState.Running))
            mFailureRequestType = try {
                val data = withContext(Dispatchers.IO) {
                    this@PagingDataSource.load(requestType)
                }
                mFlow.emit(ResultReport(requestType, RequestState.Success(data)))
                null
            } catch (e: Exception) {
                mFlow.emit(ResultReport(requestType, RequestState.Failed(e)))
                requestType
            } finally {
                isRunning.compareAndSet(true, false)
            }
        }
    }

    fun result(): Result<ResultType> = Result(
        resultReportFlow = getResultReportFlow(),
        initial = this::initial,
        refresh = this::refresh,
        retry = this::retry,
        loadAfter = if (isLoadAfter) this::loadAfter else null,
        loadBefore = if (isLoadAfter) null else this::loadBefore
    )

    abstract suspend fun load(requestType: RequestType): ResultType

}
