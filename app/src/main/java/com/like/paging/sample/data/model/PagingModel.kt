package com.like.paging.sample.data.model

import com.google.gson.annotations.SerializedName

data class PagingModel<T>(
    val nextKey: Int? = null,
    val prevKey: Int? = null,
    @SerializedName("curPage") val curPage: Int,
    @SerializedName("datas") val datas: List<T>?
)