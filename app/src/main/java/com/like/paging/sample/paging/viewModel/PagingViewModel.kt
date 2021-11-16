package com.like.paging.sample.paging.viewModel

import androidx.lifecycle.ViewModel
import com.like.paging.sample.paging.repository.PagingRepository

class PagingViewModel(private val pagingRepository: PagingRepository) : ViewModel() {
    fun getPagingDataSource() = pagingRepository.getPagingDataSource()
}