package com.like.paging.sample.paging.repository

import com.like.paging.sample.paging.dataSource.inDb.ArticlePagingDbDataSource
import com.like.paging.sample.paging.dataSource.inDb.BannerDbDataSource
import com.like.paging.sample.paging.dataSource.inDb.MergePagingDbDataSource
import com.like.paging.sample.paging.dataSource.inDb.TopArticleDbDataSource
import com.like.paging.sample.paging.dataSource.inMemory.ArticlePagingDataSource
import com.like.paging.sample.paging.dataSource.inMemory.BannerDataSource
import com.like.paging.sample.paging.dataSource.inMemory.MergePagingDataSource
import com.like.paging.sample.paging.dataSource.inMemory.TopArticleDataSource

class PagingRepository(
    private val bannerDataSource: BannerDataSource,
    private val bannerDbDataSource: BannerDbDataSource,
    private val topArticleDataSource: TopArticleDataSource,
    private val topArticleDbDataSource: TopArticleDbDataSource,
    private val articlePagingDataSource: ArticlePagingDataSource,
    private val articlePagingDbDataSource: ArticlePagingDbDataSource,
    private val mergePagingDataSource: MergePagingDataSource,
    private val mergePagingDbDataSource: MergePagingDbDataSource
) {
    private val articlePagingResult = articlePagingDataSource.pagingResult()
    private val articlePagingDbResult = articlePagingDbDataSource.pagingResult()
    private val mergePagingResult = mergePagingDataSource.pagingResult()
    private val mergePagingDbResult = mergePagingDbDataSource.pagingResult()
    fun getPagingResult() = articlePagingResult
}