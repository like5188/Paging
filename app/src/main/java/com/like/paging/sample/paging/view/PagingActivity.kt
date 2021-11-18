package com.like.paging.sample.paging.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.like.common.util.Logger
import com.like.paging.RequestType
import com.like.paging.sample.R
import com.like.paging.sample.data.db.Db
import com.like.paging.sample.databinding.ActivityPagingBinding
import com.like.paging.sample.paging.viewModel.PagingViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PagingActivity : AppCompatActivity() {
    private val mBinding by lazy {
        DataBindingUtil.setContentView<ActivityPagingBinding>(this, R.layout.activity_paging)
    }
    private val mViewModel: PagingViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding
    }

    fun initial(view: View) {
        val result = mViewModel.getPagingResult()
        lifecycleScope.launch {
            result.setRequestType(RequestType.Initial)
            result.flow.flowOn(Dispatchers.IO)
                .onStart {
                    Logger.v("Initial onStart ${Thread.currentThread().name}")
                }.onCompletion {
                    Logger.v("Initial onCompletion ${Thread.currentThread().name} $it")
                }.catch {
                    Logger.e("Initial onError ${Thread.currentThread().name} $it")
                }.flowOn(Dispatchers.Main)
                .collect {
                    Logger.d("Initial onSuccess ${Thread.currentThread().name} $it")
                }
        }
    }

    fun refresh(view: View) {
        val result = mViewModel.getPagingResult()
        lifecycleScope.launch {
            result.setRequestType(RequestType.Refresh)
            result.flow.flowOn(Dispatchers.IO)
                .onStart {
                    Logger.v("Refresh onStart ${Thread.currentThread().name}")
                }.onCompletion {
                    Logger.v("Refresh onCompletion ${Thread.currentThread().name} $it")
                }.catch {
                    Logger.e("Refresh onError ${Thread.currentThread().name} $it")
                }.flowOn(Dispatchers.Main)
                .collect {
                    Logger.d("Refresh onSuccess ${Thread.currentThread().name} $it")
                }
        }
    }

    fun loadAfter(view: View) {
        val result = mViewModel.getPagingResult()
        lifecycleScope.launch {
            result.setRequestType(RequestType.After)
            result.flow.flowOn(Dispatchers.IO)
                .onStart {
                    Logger.v("After onStart ${Thread.currentThread().name}")
                }.onCompletion {
                    Logger.v("After onCompletion ${Thread.currentThread().name} $it")
                }.catch {
                    Logger.e("After onError ${Thread.currentThread().name} $it")
                }.flowOn(Dispatchers.Main)
                .collect {
                    Logger.d("After onSuccess ${Thread.currentThread().name} $it")
                }
        }
    }

    fun loadBefore(view: View) {
        val result = mViewModel.getPagingResult()
        lifecycleScope.launch {
            result.setRequestType(RequestType.Before)
            result.flow.flowOn(Dispatchers.IO)
                .onStart {
                    Logger.v("Before onStart ${Thread.currentThread().name}")
                }.onCompletion {
                    Logger.v("Before onCompletion ${Thread.currentThread().name} $it")
                }.catch {
                    Logger.e("Before onError ${Thread.currentThread().name} $it")
                }.flowOn(Dispatchers.Main)
                .collect {
                    Logger.d("Before onSuccess ${Thread.currentThread().name} $it")
                }
        }
    }

    fun retry(view: View) {
    }

    fun clearDb(view: View) {
        lifecycleScope.launch(Dispatchers.IO) {
            Db.getInstance(application).bannerEntityDao().deleteAll()
            Db.getInstance(application).topArticleEntityDao().deleteAll()
            Db.getInstance(application).articleEntityDao().deleteAll()
        }
    }

    fun queryDb(view: View) {
        lifecycleScope.launch(Dispatchers.IO) {
            Db.getInstance(application).bannerEntityDao().getAll().forEach {
                Logger.i(it.toString())
            }
            Db.getInstance(application).topArticleEntityDao().getAll().forEach {
                Logger.i(it.toString())
            }
            Db.getInstance(application).articleEntityDao().getAll().forEach {
                Logger.i(it.toString())
            }
        }
    }
}
