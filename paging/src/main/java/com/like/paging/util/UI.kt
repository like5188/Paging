package com.like.paging.util

import com.like.paging.RequestState
import com.like.paging.RequestType
import com.like.paging.ResultReport
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

/**
 * 绑定不分页数据到界面。
 * 成功失败回调，进度条显示隐藏。
 *
 * @param onData            请求成功后回调。
 * @param onError           请求失败时回调。
 * @param show              显示进度条
 * @param hide              隐藏进度条
 */
suspend fun <ResultType> (suspend () -> ResultType).bind(
    onData: suspend (ResultType) -> Unit,
    onError: (suspend (Throwable) -> Unit)? = null,
    show: (() -> Unit)? = null,
    hide: (() -> Unit)? = null,
) = withContext(Dispatchers.Main) {
    show?.invoke()
    try {
        val result = withContext(Dispatchers.IO) {
            this@bind()
        }
        onData(result)
    } catch (e: Exception) {
        onError?.invoke(e)
    } finally {
        hide?.invoke()
    }
}

/**
 * 绑定分页数据到界面。
 * 成功失败回调，进度条显示隐藏。
 *
 * @param onInitialOrRefresh        初始化或者刷新成功时回调。
 * @param onInitialError            初始化失败时回调。
 * @param onLoadMore                加载更多成功时回调。
 * @param onLoadMoreError           加载更多失败时回调。
 * @param show                      初始化或者刷新开始时显示进度条
 * @param hide                      初始化或者刷新成功或者失败时隐藏进度条
 * @param onFailed                  失败回调，如果需要做其它错误处理，可以从这里获取。
 * @param onSuccess                 成功回调，如果需要结果做其它逻辑处理，可以从这里获取。
 */
fun <ResultType> Flow<ResultReport<ResultType>>.bind(
    onInitialOrRefresh: suspend (ResultType) -> Unit,
    onInitialError: (suspend (Throwable) -> Unit)? = null,
    onLoadMore: suspend (ResultType) -> Unit,
    onLoadMoreError: suspend (Throwable) -> Unit,
    show: (() -> Unit)? = null,
    hide: (() -> Unit)? = null,
    onFailed: (suspend (RequestType, Throwable) -> Unit)? = null,
    onSuccess: (suspend (RequestType, ResultType) -> Unit)? = null,
): Flow<ResultReport<ResultType>> {
    return onEach { resultReport ->
        val state = resultReport.state
        val type = resultReport.type
        // 数据处理
        when {
            (type is RequestType.Initial || type is RequestType.Refresh) && state is RequestState.Success -> {
                onInitialOrRefresh(state.data)
            }
            type is RequestType.Initial && state is RequestState.Failed -> {
                onInitialError?.invoke(state.throwable)
            }
            type is RequestType.After || type is RequestType.Before -> {
                when (state) {
                    is RequestState.Success -> {
                        onLoadMore(state.data)
                    }
                    is RequestState.Failed -> {
                        onLoadMoreError(state.throwable)
                    }
                }
            }
        }
        // 进度处理
        if (type is RequestType.Initial || type is RequestType.Refresh) {
            when (state) {
                is RequestState.Running -> {
                    show?.invoke()
                }
                else -> {
                    hide?.invoke()
                }
            }
        }
        // 成功失败回调处理
        when (state) {
            is RequestState.Failed -> {
                onFailed?.invoke(type, state.throwable)
            }
            is RequestState.Success -> {
                onSuccess?.invoke(type, state.data)
            }
        }
    }
}
