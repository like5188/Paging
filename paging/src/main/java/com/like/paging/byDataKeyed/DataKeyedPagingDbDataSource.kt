package com.like.paging.byDataKeyed

import android.util.Log
import androidx.annotation.WorkerThread
import com.like.paging.RequestType

/**
 * 包含数据库的数据源。
 */
abstract class DataKeyedPagingDbDataSource<Key : Any, ResultType>(pageSize: Int, isLoadAfter: Boolean = true) :
    DataKeyedPagingDataSource<Key, ResultType>(pageSize, isLoadAfter) {
    companion object {
        private val TAG = DataKeyedPagingDbDataSource::class.java.simpleName
    }

    final override suspend fun load(requestType: RequestType, key: Key?, pageSize: Int): ResultType {
        var data = loadFromDb(requestType, key, pageSize)
        if (shouldFetch(requestType, data)) {
            Log.d(TAG, "即将从网络获取数据并存入数据库中")
            fetchFromNetworkAndSaveToDb(requestType, key, pageSize)
            Log.d(TAG, "即将重新从数据库获取数据")
            data = loadFromDb(requestType, key, pageSize)
        }
        Log.d(TAG, "从数据库获取到了数据：$data")
        return data
    }

    /**
     * 从数据库中获取数据
     *
     * @param requestType   请求类型：[RequestType]
     * @param key           上一页、下一页的标记。
     * @param pageSize      每页加载数量
     */
    @WorkerThread
    protected abstract suspend fun loadFromDb(requestType: RequestType, key: Key?, pageSize: Int): ResultType

    /**
     * 是否应该从网络获取数据。
     */
    @WorkerThread
    protected abstract fun shouldFetch(requestType: RequestType, resultType: ResultType): Boolean

    /**
     * 从网络获取到数据并存入数据库中。
     *
     * @param requestType   请求类型：[RequestType]
     * @param key           上一页、下一页的标记。
     * @param pageSize      每页加载数量
     */
    @WorkerThread
    protected abstract suspend fun fetchFromNetworkAndSaveToDb(requestType: RequestType, key: Key?, pageSize: Int)
}