package com.like.paging

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach

/**
 * 封装了各种请求操作及其请求结果。
 */
data class Result<ResultType>(
    // 结果报告
    val flow: Flow<ResultReport<ResultType>>,
    // 初始化操作
    val initial: suspend () -> Unit,
    // 刷新操作
    val refresh: suspend () -> Unit,
    // 失败重试操作
    val retry: suspend () -> Unit,
    // 往后加载更多，不分页时不用设置
    val loadAfter: (suspend () -> Unit)? = null,
    // 往前加载更多，不分页时不用设置
    val loadBefore: (suspend () -> Unit)? = null
)

/**
 * 绑定分页数据到界面。(线程安全)
 * 成功失败回调，进度条显示隐藏。
 *
 * @param onSuccess         请求成功后回调。
 * @param onError           请求失败时回调。
 * @param show              初始化或者刷新开始时显示进度条
 * @param hide              初始化或者刷新成功或者失败时隐藏进度条
 * @return 经过处理后的 Flow
 */
fun <ResultType> Result<ResultType>.bind(
    onSuccess: suspend (RequestType, ResultType) -> Unit,
    onError: (suspend (RequestType, Throwable) -> Unit)? = null,
    show: (() -> Unit)? = null,
    hide: (() -> Unit)? = null,
): Flow<ResultReport<ResultType>> {
    return flow.onEach { resultReport ->
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
                onSuccess(type, state.data)
            }
            is RequestState.Failed -> {
                onError?.invoke(type, state.throwable)
            }
        }
    }.flowOn(Dispatchers.Main)
}