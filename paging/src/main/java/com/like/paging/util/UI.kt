package com.like.paging.util

import com.like.paging.RequestState
import com.like.paging.RequestType
import com.like.paging.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

/**
 * 绑定不分页数据到界面。
 * 执行获取数据的 suspend 函数，并控制进度条的显示隐藏。
 *
 * @param onData            有数据时回调。
 * @param onEmpty           没有数据时回调。
 * @param onError           失败时回调。
 * @param show              初始化或者刷新开始时显示进度条
 * @param hide              初始化或者刷新成功或者失败时隐藏进度条
 */
suspend fun <ValueInList> (suspend () -> List<ValueInList>?).bind(
    onData: (List<ValueInList>) -> Unit,
    onEmpty: (() -> Unit)? = null,
    onError: ((Throwable) -> Unit)? = null,
    show: (() -> Unit)? = null,
    hide: (() -> Unit)? = null,
) = withContext(Dispatchers.Main) {
    show?.invoke()
    try {
        val list = withContext(Dispatchers.IO) {
            this@bind()
        }
        if (list.isNullOrEmpty()) {
            onEmpty?.invoke()
        } else {
            onData(list)
        }
    } catch (e: Exception) {
        onError?.invoke(e)
    } finally {
        hide?.invoke()
    }
}

/**
 * 绑定分页数据的 Result 绑定到界面。
 *
 * @param onInitialOrRefresh        初始化或者刷新成功并且有数据时回调。
 * @param onLoadMore                加载更多成功并且有数据时回调。
 * @param onLoadMoreEnd             加载更多成功并且没有数据时回调。
 * @param onLoadMoreError           加载更多失败时回调。
 * @param onInitialOrRefreshEmpty   初始化或者刷新成功并且没有数据时回调。
 * @param onInitialError            初始化失败时回调。
 * @param show                      初始化或者刷新开始时显示进度条
 * @param hide                      初始化或者刷新成功或者失败时隐藏进度条
 * @param onFailed                  失败回调，如果需要做其它错误处理，可以从这里获取。
 * @param onSuccess                 成功回调，如果需要结果做其它逻辑处理，可以从这里获取。
 */
fun <ValueInList> Result<List<ValueInList>?>.bind(
    onInitialOrRefresh: (List<ValueInList>) -> Unit,
    onLoadMore: (List<ValueInList>) -> Unit,
    onLoadMoreEnd: () -> Unit,
    onLoadMoreError: (Throwable) -> Unit,
    onInitialOrRefreshEmpty: (() -> Unit)? = null,
    onInitialError: ((Throwable) -> Unit)? = null,
    show: (() -> Unit)? = null,
    hide: (() -> Unit)? = null,
    onFailed: (suspend (RequestType, Throwable) -> Unit)? = null,
    onSuccess: (suspend (RequestType, List<ValueInList>?) -> Unit)? = null,
): Result<List<ValueInList>?> {
    resultReportFlow.onEach { resultReport ->
        val state = resultReport.state
        val type = resultReport.type
        // 数据处理
        when {
            (type is RequestType.Initial || type is RequestType.Refresh) && state is RequestState.Success -> {
                val list = state.data
                if (list.isNullOrEmpty()) {
                    onInitialOrRefreshEmpty?.invoke()
                } else {
                    onInitialOrRefresh(list)
                }
            }
            type is RequestType.Initial && state is RequestState.Failed -> {
                onInitialError?.invoke(state.throwable)
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
    return this
}
