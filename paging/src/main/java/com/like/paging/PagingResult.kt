package com.like.paging

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

/**
 * @param flow              数据流。在每次调用[Flow.collect]来触发请求之前都需要调用[setRequestType]设置请求类型
 * @param setRequestType    设置请求类型
 */
data class PagingResult<ResultType>(
    var flow: Flow<ResultType>,
    val setRequestType: (RequestType) -> Unit,
)
