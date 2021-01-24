package com.like.paging.sample.paging.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.like.common.util.Logger
import com.like.paging.sample.data.model.BannerInfo
import com.like.paging.sample.data.model.TopArticleEntity
import com.like.paging.sample.paging.repository.PagingRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PagingViewModel(private val pagingRepository: PagingRepository) : ViewModel() {
    init {
        viewModelScope.launch {
            pagingRepository.getPagingResult().resultReportFlow.collect {
                Logger.d(it)
            }
        }
    }

    fun getPagingResult() = pagingRepository.getPagingResult()

    suspend fun getBanner(isRefresh: Boolean = false): List<BannerInfo>? {
        return pagingRepository.getBanner(isRefresh)
    }

    suspend fun getTopArticle(isRefresh: Boolean = false): List<TopArticleEntity>? {
        return pagingRepository.getTopArticle(isRefresh)
    }

}