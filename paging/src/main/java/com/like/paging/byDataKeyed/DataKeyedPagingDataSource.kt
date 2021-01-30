package com.like.paging.byDataKeyed

import androidx.annotation.WorkerThread
import com.like.paging.RequestType
import com.like.paging.PagingDataSource

/**
 * 根据接口返回的数据，由用户来确定分页 key 的分页数据源。
 *
 * @param Key           分页标记数据类型
 */
abstract class DataKeyedPagingDataSource<Key : Any, ResultType>(private val pageSize: Int, isLoadAfter: Boolean = true) :
    PagingDataSource<ResultType>(isLoadAfter) {
    private var key: Key? = null

    final override suspend fun load(requestType: RequestType): ResultType {
        if (requestType is RequestType.Initial || requestType is RequestType.Refresh) {
            key = null
        }
        return load(requestType, key, pageSize).apply {
            key = getKey(this)
        }
    }

    /**
     * 根据返回的数据获取上一页或者下一页的 key。
     */
    protected abstract fun getKey(data: ResultType): Key?

    /**
     * @param requestType   请求类型：[RequestType]
     * @param key           上一页、下一页的标记。
     * @param pageSize      每页加载数量
     */
    @WorkerThread
    abstract suspend fun load(requestType: RequestType, key: Key?, pageSize: Int): ResultType

}