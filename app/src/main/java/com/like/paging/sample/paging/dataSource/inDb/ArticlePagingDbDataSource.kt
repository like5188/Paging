package com.like.paging.sample.paging.dataSource.inDb

import android.content.Context
import com.like.common.util.isInternetAvailable
import com.like.paging.RequestType
import com.like.paging.sample.data.netWork.RetrofitUtils
import com.like.paging.byPageNoKeyed.PageNoKeyedPagingDbDataSource
import com.like.paging.sample.MyApplication
import com.like.paging.sample.data.db.ArticleEntityDao
import com.like.paging.sample.data.model.ArticleEntity

class ArticlePagingDbDataSource(private val context: Context, private val articleEntityDao: ArticleEntityDao) :
    PageNoKeyedPagingDbDataSource<List<ArticleEntity>?>(MyApplication.PAGE_SIZE) {

    override fun getInitialPage(): Int {
        return 0
    }

    override suspend fun loadFromDb(
        requestType: RequestType,
        pageNo: Int,
        pageSize: Int
    ): List<ArticleEntity>? {
        return articleEntityDao.getPage(pageNo * pageSize, pageSize)
    }

    override fun shouldFetch(requestType: RequestType, resultType: List<ArticleEntity>?): Boolean {
        return context.isInternetAvailable() && (resultType.isNullOrEmpty() || requestType == RequestType.Refresh)
    }

    override suspend fun fetchFromNetworkAndSaveToDb(
        requestType: RequestType,
        pageNo: Int,
        pageSize: Int
    ) {
        val data = RetrofitUtils.retrofitApi.getArticle(pageNo).getDataIfSuccess()?.datas
        if (!data.isNullOrEmpty()) {
            if (requestType == RequestType.Refresh) {
                articleEntityDao.deleteAll()
            }
            articleEntityDao.insertAll(data)
        }
    }

}