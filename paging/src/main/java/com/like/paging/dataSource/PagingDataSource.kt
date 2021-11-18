package com.like.paging.dataSource

import com.like.paging.RequestType
import com.like.paging.Result
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * 分页数据源基类。
 *
 * @param ResultType    返回的数据类型
 */
abstract class PagingDataSource<ResultType> {
    private var mCurRequestType: RequestType = RequestType.Initial
    private val mDataFlow: Flow<ResultType> = flow {
        val data = withContext(Dispatchers.IO) {
            this@PagingDataSource.load(mCurRequestType)
        }
        emit(data)
    }

    fun result(): Result<ResultType> = Result(
        flow = mDataFlow,
        setRequestType = { mCurRequestType = it },
        getRequestType = { mCurRequestType }
    )

    abstract suspend fun load(requestType: RequestType): ResultType

}
