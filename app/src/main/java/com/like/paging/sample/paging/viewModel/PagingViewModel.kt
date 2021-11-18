package com.like.paging.sample.paging.viewModel

import androidx.lifecycle.ViewModel
import com.like.common.util.Logger
import com.like.paging.sample.paging.repository.PagingRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen

class PagingViewModel(private val pagingRepository: PagingRepository) : ViewModel() {
    fun getPagingResult() = pagingRepository.getPagingResult().apply {
        flow = flow
            .retryWhen { cause, attempt ->
                Logger.w("retryWhen ${Thread.currentThread().name}")
                cause.message == "test error 0" && attempt == 0L
            }.map {
                it?.take(1)
            }
    }
}