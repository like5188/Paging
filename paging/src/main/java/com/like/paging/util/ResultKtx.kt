package com.like.paging.util

import com.like.paging.RequestState
import com.like.paging.RequestType
import com.like.paging.Result
import com.like.paging.ResultReport
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

/**
 * [com.like.paging.Result] 扩展功能，方便绑定到 RecyclerView。
 */

/**
 * 开始搜集数据。
 *
 * @param onFailed      失败回调，如果需要做其它错误处理，可以从这里获取。
 * @param onSuccess     成功回调，如果需要结果做其它逻辑处理，可以从这里获取。
 */
suspend fun <ResultType> Result<ResultType>.collect(
    onFailed: (suspend (RequestType, Throwable) -> Unit)? = null,
    onSuccess: (suspend (RequestType, ResultType) -> Unit)? = null,
) {
    resultReportFlow.collect { resultReport ->
        val state = resultReport.state
        val type = resultReport.type
        when (state) {
            is RequestState.Failed -> {
                onFailed?.invoke(type, state.throwable)
            }
            is RequestState.Success<ResultType> -> {
                onSuccess?.invoke(type, state.data)
            }
        }
    }
}

/**
 * 绑定进度条。
 * 初始化或者刷新时控制进度条的显示隐藏。
 *
 * @param show          初始化或者刷新开始时显示进度条
 * @param hide          初始化或者刷新成功或者失败时隐藏进度条
 */
fun <ResultType> Result<ResultType>.bindProgress(
    show: () -> Unit,
    hide: () -> Unit,
): Result<ResultType> {
    val newResultReportFlow = resultReportFlow.onEach { resultReport ->
        val state = resultReport.state
        val type = resultReport.type
        if (type is RequestType.Initial || type is RequestType.Refresh) {
            when (state) {
                is RequestState.Running -> {
                    show()
                }
                else -> {
                    hide()
                }
            }
        }
    }
    return updateResultReportFlow(newResultReportFlow)
}

/**
 * 绑定列表数据
 *
 * @param onData           初始化或者刷新成功并且有数据时回调。
 * @param onEmpty      初始化或者刷新成功并且没有数据时回调。
 * @param onError      初始化失败时回调。
 * @param onLoadMore        加载更多成功并且有数据时回调。
 * @param onLoadMoreEnd     加载更多成功并且没有数据时回调。
 * @param onLoadMoreError   加载更多失败时回调。
 */
fun <ValueInList> Result<List<ValueInList>?>.bindList(
    onData: (List<ValueInList>) -> Unit,
    onEmpty: (() -> Unit)? = null,
    onError: ((Throwable) -> Unit)? = null,
    onLoadMore: ((List<ValueInList>) -> Unit)? = null,
    onLoadMoreEnd: (() -> Unit)? = null,
    onLoadMoreError: ((Throwable) -> Unit)? = null,
): Result<List<ValueInList>?> {
    val newResultReportFlow = resultReportFlow.onEach { resultReport ->
        val state = resultReport.state
        val type = resultReport.type
        when {
            (type is RequestType.Initial || type is RequestType.Refresh) && state is RequestState.Success -> {
                val list = state.data
                if (list.isNullOrEmpty()) {
                    onEmpty?.invoke()
                } else {
                    onData(list)
                }
            }
            type is RequestType.Initial && state is RequestState.Failed -> {
                onError?.invoke(state.throwable)
            }
            type is RequestType.After || type is RequestType.Before -> {
                when (state) {
                    is RequestState.Success -> {
                        val list = state.data
                        if (list.isNullOrEmpty()) {
                            // 到底了
                            onLoadMoreEnd?.invoke()
                        } else {
                            onLoadMore?.invoke(list)
                        }
                    }
                    is RequestState.Failed -> {
                        onLoadMoreError?.invoke(state.throwable)
                    }
                }
            }
        }
    }
    return updateResultReportFlow(newResultReportFlow)
}

fun <ResultType> Result<ResultType>.updateResultReportFlow(newResultReportFlow: Flow<ResultReport<ResultType>>): Result<ResultType> {
    return Result(newResultReportFlow, initial, refresh, retry, loadAfter, loadBefore)
}