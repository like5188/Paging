package com.like.paging

/**
 * 结果报告。
 * 包括请求类型[RequestType]、请求状态[RequestState]
 */
data class ResultReport<out ResultType>(val type: RequestType, val state: RequestState<ResultType>) {

    override fun toString(): String {
        return "ResultReport($type $state)"
    }

}