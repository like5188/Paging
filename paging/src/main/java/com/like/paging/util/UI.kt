package com.like.paging.util

import com.like.paging.RequestState
import com.like.paging.RequestType
import com.like.paging.Result
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
 * @param onData            请求成功后回调。
 * @param onError           请求失败时回调。
 * @param show              初始化或者刷新开始时显示进度条
 * @param hide              初始化或者刷新成功或者失败时隐藏进度条
 * @return 经过处理后的 Flow
 */
fun <ResultType> Result<ResultType>.bind(
    onData: suspend (RequestType, ResultType) -> Unit,
    onError: (suspend (RequestType, Throwable) -> Unit)? = null,
    show: (() -> Unit)? = null,
    hide: (() -> Unit)? = null,
): Flow<ResultReport<ResultType>> {
    return resultReportFlow.onEach { resultReport ->
        val state = resultReport.state
        val type = resultReport.type
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
            is RequestState.Success -> {
                onData(type, state.data)
            }
            is RequestState.Failed -> {
                onError?.invoke(type, state.throwable)
            }
        }
    }
}
