package com.like.paging.sample.paging.dataSource.inDb

import android.content.Context
import com.like.common.util.isInternetAvailable
import com.like.paging.RequestType
import com.like.paging.dataSource.byPageNoKeyed.PageNoKeyedPagingDataSource
import com.like.paging.dbHelper.IPagingDbHelper
import com.like.paging.sample.data.db.ArticleEntityDao
import com.like.paging.sample.data.model.ArticleEntity
import com.like.paging.sample.data.netWork.RetrofitUtils

class ArticlePagingDbDataSource(private val context: Context, private val articleEntityDao: ArticleEntityDao) :
    PageNoKeyedPagingDataSource<List<ArticleEntity>?>(0) {
    private val mDbHelper = object : IPagingDbHelper<Int, List<ArticleEntity>?> {
        override suspend fun loadFromDb(requestType: RequestType, key: Int?, pageSize: Int): List<ArticleEntity>? {
            return articleEntityDao.getPage((key ?: 0) * pageSize, pageSize)
        }

        override fun shouldFetch(requestType: RequestType, result: List<ArticleEntity>?): Boolean {
            return context.isInternetAvailable() && (result.isNullOrEmpty() || requestType == RequestType.Refresh)
        }

        override suspend fun fetchFromNetworkAndSaveToDb(requestType: RequestType, key: Int?, pageSize: Int) {
            val data = RetrofitUtils.retrofitApi.getArticle(key ?: 0).getDataIfSuccess()?.datas
            if (!data.isNullOrEmpty()) {
                if (requestType == RequestType.Refresh) {
                    articleEntityDao.deleteAll()
                }
                articleEntityDao.insertAll(data)
            }
        }
    }

    override suspend fun load(requestType: RequestType, pageNo: Int, pageSize: Int): List<ArticleEntity>? {
        return mDbHelper.load(requestType, pageNo, pageSize)
    }

}