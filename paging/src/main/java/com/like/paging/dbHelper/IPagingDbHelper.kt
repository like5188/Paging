package com.like.paging.dbHelper

import com.like.paging.RequestType

/**
 * 分页数据需要存储数据库时，可以使用此接口，此接口的[load]方法实现了一种存储策略。
 */
interface IPagingDbHelper<Key : Any, ResultType> {

    suspend fun load(requestType: RequestType, key: Key?, pageSize: Int): ResultType {
        var data = loadFromDb(requestType, key, pageSize)
        if (shouldFetch(requestType, data)) {
            //即将从网络获取数据并存入数据库中
            fetchFromNetworkAndSaveToDb(requestType, key, pageSize)
            //即将重新从数据库获取数据
            data = loadFromDb(requestType, key, pageSize)
        }
        //从数据库获取到了数据
        return data
    }

    /**
     * 从数据库中获取数据
     *
     * @param requestType   请求类型：[RequestType]
     * @param key           分页标记
     * @param pageSize      每页加载数量
     */
    suspend fun loadFromDb(requestType: RequestType, key: Key?, pageSize: Int): ResultType

    /**
     * 是否应该从网络获取数据。
     */
    fun shouldFetch(requestType: RequestType, result: ResultType): Boolean

    /**
     * 从网络获取数据并存储数据库中。
     */
    suspend fun fetchFromNetworkAndSaveToDb(requestType: RequestType, key: Key?, pageSize: Int)
}