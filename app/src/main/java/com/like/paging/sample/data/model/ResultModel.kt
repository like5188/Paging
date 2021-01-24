package com.like.paging.sample.data.model

import com.google.gson.annotations.SerializedName

data class ResultModel<T>(
    @SerializedName("errorCode") val errorCode: Int,
    @SerializedName("errorMsg") val errorMsg: String?,
    @SerializedName("data") val data: T?
) {

    /**
     * 成功就返回数据，否则抛异常出来，由 [com.like.datasource.Result.liveState] 抓取并发射出来。
     */
    fun getDataIfSuccess(): T? {
        if (errorCode != 0) {
            val errorMsg = if (this.errorMsg.isNullOrEmpty()) {
                "unknown error"
            } else {
                this.errorMsg
            }
            throw RuntimeException(errorMsg)
        }
        return this.data
    }
}