package com.like.paging.dataSource

import com.like.paging.RequestType
import com.like.paging.Result
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * 分页数据源基类。管理初始化、刷新、往后加载更多、往前加载更多。
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
        requestType = { mCurRequestType },
        initial = { mCurRequestType = RequestType.Initial },
        refresh = { mCurRequestType = RequestType.Refresh },
        after = { mCurRequestType = RequestType.After },
        before = { mCurRequestType = RequestType.Before },
    )

    abstract suspend fun load(requestType: RequestType): ResultType

}
