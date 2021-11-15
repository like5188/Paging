package com.like.paging

import kotlinx.coroutines.flow.Flow

/**
 * 封装了各种请求操作及其请求结果。
 */
data class Result<ResultType>(
    // 初始化操作
    val initial: () -> Flow<ResultType>,
    // 刷新操作
    val refresh: () -> Flow<ResultType>,
    // 往后加载更多，不分页时不用设置
    val loadAfter: (() -> Flow<ResultType>)? = null,
    // 往前加载更多，不分页时不用设置
    val loadBefore: (() -> Flow<ResultType>)? = null
)
