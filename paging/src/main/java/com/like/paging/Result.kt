package com.like.paging

import kotlinx.coroutines.flow.Flow

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