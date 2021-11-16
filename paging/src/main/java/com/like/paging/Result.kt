package com.like.paging

import kotlinx.coroutines.flow.Flow

/**
 * 封装了各种请求操作及其请求结果。
 */
data class Result<ResultType>(
    // 初始化操作
    var initial: () -> Flow<ResultType>,
    // 刷新操作
    var refresh: () -> Flow<ResultType>,
    // 往后加载更多，不分页时不用设置
    var loadAfter: (() -> Flow<ResultType>)? = null,
    // 往前加载更多，不分页时不用设置
    var loadBefore: (() -> Flow<ResultType>)? = null
)
