package com.like.paging.dataSource

import com.like.paging.PagingResult
import com.like.paging.RequestType
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * 分页请求数据源基类。
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

    fun pagingResult(): PagingResult<ResultType> = PagingResult(
        flow = mDataFlow,
        setRequestType = { mCurRequestType = it }
    )

    abstract suspend fun load(requestType: RequestType): ResultType

}
