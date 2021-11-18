package com.like.paging

import com.like.paging.util.ConcurrencyHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

/**
 * 封装了初始化、刷新、往后加载更多、往前加载更多这四种操作。
 * 请求并发处理规则：
 * 1、初始化、刷新：如果有操作正在执行，则取消正在执行的操作，执行新操作。
 * 2、往后加载更多、往前加载更多：如果有操作正在执行，则放弃新操作，否则执行新操作。
 */
data class Result<ResultType>(
    var flow: Flow<ResultType>,
    private val setRequestType: (RequestType) -> Unit,
) {
    private val mConcurrencyHelper = ConcurrencyHelper()

    /**
     * 初始化或者刷新开始时显示进度条
     */
    var show: (() -> Unit)? = null

    /**
     * 初始化或者刷新完成时隐藏进度条
     */
    var hide: (() -> Unit)? = null

    /**
     * 请求失败时回调
     */
    var onError: (suspend (RequestType, Throwable) -> Unit)? = null

    /**
     * 请求成功时回调
     */
    var onSuccess: (suspend (RequestType, ResultType) -> Unit)? = null

    suspend fun initial() {
        mConcurrencyHelper.cancelPreviousThenRun {
            setRequestType(RequestType.Initial)
            collect(RequestType.Initial, show, hide, onError, onSuccess)
        }
    }

    suspend fun refresh() {
        mConcurrencyHelper.cancelPreviousThenRun {
            setRequestType(RequestType.Refresh)
            collect(RequestType.Refresh, show, hide, onError, onSuccess)
        }
    }

    suspend fun after() {
        mConcurrencyHelper.dropIfPreviousRunning {
            setRequestType(RequestType.After)
            collect(RequestType.After, show, hide, onError, onSuccess)
        }
    }

    suspend fun before() {
        mConcurrencyHelper.dropIfPreviousRunning {
            setRequestType(RequestType.Before)
            collect(RequestType.Before, show, hide, onError, onSuccess)
        }
    }

    /**
     * 收集分页数据
     *
     * @param show              初始化或者刷新开始时显示进度条
     * @param hide              初始化或者刷新完成时隐藏进度条
     * @param onError           请求失败时回调。
     * @param onSuccess         请求成功时回调。
     */
    private suspend fun collect(
        requestType: RequestType,
        show: (() -> Unit)? = null,
        hide: (() -> Unit)? = null,
        onError: (suspend (RequestType, Throwable) -> Unit)? = null,
        onSuccess: (suspend (RequestType, ResultType) -> Unit)? = null,
    ) {
        flow.flowOn(Dispatchers.IO)
            .onStart {
                if (requestType is RequestType.Initial || requestType is RequestType.Refresh) {
                    show?.invoke()
                }
            }.onCompletion {
                if (requestType is RequestType.Initial || requestType is RequestType.Refresh) {
                    hide?.invoke()
                }
            }.catch {
                onError?.invoke(requestType, it)
            }.flowOn(Dispatchers.Main)
            .collect {
                onSuccess?.invoke(requestType, it)
            }
    }

}
