package com.like.paging

import kotlinx.coroutines.flow.Flow

data class PagingResult<ResultType>(
    var flow: Flow<ResultType>,
    val setRequestType: (RequestType) -> Unit,
)
