package com.like.paging.sample.paging.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.like.common.util.Logger
import com.like.paging.sample.data.model.BannerInfo
import com.like.paging.sample.data.model.TopArticleEntity
import com.like.paging.sample.paging.repository.PagingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PagingViewModel(private val pagingRepository: PagingRepository) : ViewModel() {
    init {
        // 分页使用
        viewModelScope.launch {
            pagingRepository.getPagingResult().resultReportFlow.collect {
                Logger.d(it)
            }
        }
    }

    // 分页使用
    fun getPagingResult() = pagingRepository.getPagingResult()

    // 不分页不需要进度条
    suspend fun getBanner(isRefresh: Boolean = false): List<BannerInfo>? {
        return pagingRepository.getBanner(isRefresh)
    }

    // 不分页需要进度条
    fun getTopArticleFlow(isRefresh: Boolean = false): Flow<List<TopArticleEntity>?> = flow {
        emit(pagingRepository.getTopArticle(isRefresh))
    }.onStart {
        Logger.d("onStart ${Thread.currentThread().name}")
    }.onCompletion {
        Logger.d("onCompletion ${Thread.currentThread().name} $it")
    }.catch {
        Logger.d("catch ${Thread.currentThread().name} $it")
    }.flowOn(Dispatchers.IO)

}