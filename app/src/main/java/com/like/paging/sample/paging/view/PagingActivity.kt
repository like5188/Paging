package com.like.paging.sample.paging.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.like.common.util.Logger
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
//        val result = mViewModel.getPagingResult()
//        result.initial()
//        lifecycleScope.launch {
//            result.flow
//                .flowOn(Dispatchers.IO)
//                .onStart {
//                    Logger.v("onStart ${Thread.currentThread().name} ${result.requestType()}")
//                }.onCompletion {
//                    Logger.i("onCompletion ${Thread.currentThread().name} ${result.requestType()} $it")
//                }.catch {
//                    Logger.e("catch ${Thread.currentThread().name} ${result.requestType()} $it")
//                }.flowOn(Dispatchers.Main)
//                .collect {
//                    Logger.d("collect ${Thread.currentThread().name} ${result.requestType()} $it")
//                }
//        }
    }

    fun refresh(view: View) {
//        val result = mViewModel.getPagingResult()
//        result.refresh()
//        lifecycleScope.launch {
//            result.flow
//                .flowOn(Dispatchers.IO)
//                .onStart {
//                    Logger.v("onStart ${Thread.currentThread().name} ${result.requestType()}")
//                }.onCompletion {
//                    Logger.i("onCompletion ${Thread.currentThread().name} ${result.requestType()} $it")
//                }.catch {
//                    Logger.e("catch ${Thread.currentThread().name} ${result.requestType()} $it")
//                }.flowOn(Dispatchers.Main)
//                .collect {
//                    Logger.d("collect ${Thread.currentThread().name} ${result.requestType()} $it")
//                }
//        }
    }

    fun loadAfter(view: View) {
//        val result = mViewModel.getPagingResult()
//        result.after()
//        lifecycleScope.launch {
//            result.flow
//                .flowOn(Dispatchers.IO)
//                .onStart {
//                    Logger.v("onStart ${Thread.currentThread().name} ${result.requestType()}")
//                }.onCompletion {
//                    Logger.i("onCompletion ${Thread.currentThread().name} ${result.requestType()} $it")
//                }.catch {
//                    Logger.e("catch ${Thread.currentThread().name} ${result.requestType()} $it")
//                }.flowOn(Dispatchers.Main)
//                .collect {
//                    Logger.d("collect ${Thread.currentThread().name} ${result.requestType()} $it")
//                }
//        }
    }

    fun loadBefore(view: View) {
//        val result = mViewModel.getPagingResult()
//        result.before()
//        lifecycleScope.launch {
//            result.flow
//                .flowOn(Dispatchers.IO)
//                .onStart {
//                    Logger.v("onStart ${Thread.currentThread().name} ${result.requestType()}")
//                }.onCompletion {
//                    Logger.i("onCompletion ${Thread.currentThread().name} ${result.requestType()} $it")
//                }.catch {
//                    Logger.e("catch ${Thread.currentThread().name} ${result.requestType()} $it")
//                }.flowOn(Dispatchers.Main)
//                .collect {
//                    Logger.d("collect ${Thread.currentThread().name} ${result.requestType()} $it")
//                }
//        }
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
