package com.like.paging.dataSource.byDataKeyed

import com.like.paging.RequestType
import com.like.paging.dataSource.PagingDataSource

/**
 * 根据接口返回的数据，由用户来确定分页 key 的分页数据源。
 *
 * @param Key           分页标记数据类型
 * @param pageSize      每页加载数量。默认为 10
 */
abstract class DataKeyedPagingDataSource<Key : Any, ResultType>(
    private val pageSize: Int = 10
) : PagingDataSource<ResultType>() {
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
    protected abstract fun getKey(result: ResultType): Key?

    /**
     * @param requestType   请求类型：[RequestType]
     * @param key           上一页、下一页的标记。当请求类型为：[RequestType.Initial]或者[RequestType.Refresh]时，[key]为null
     * @param pageSize      每页加载数量
     */
    abstract suspend fun load(requestType: RequestType, key: Key?, pageSize: Int): ResultType

}