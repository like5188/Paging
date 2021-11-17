package com.like.paging

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

/**
 * 封装了各种请求操作及其请求结果。
 *
 * @param flow          数据流。在每次设置请求类型后，都需要调用[Flow.collect]来触发请求。
 * @param requestType   请求类型。[RequestType]
 * @param initial       设置请求类型为[RequestType.Initial]
 * @param refresh       设置请求类型为[RequestType.Refresh]
 * @param after         设置请求类型为[RequestType.After]
 * @param before        设置请求类型为[RequestType.Before]
 */
data class Result<ResultType>(
    var flow: Flow<ResultType>,
    val requestType: () -> RequestType,
    val initial: () -> Unit,
    val refresh: () -> Unit,
    val after: () -> Unit,
    val before: () -> Unit
)
