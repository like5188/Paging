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
            result.initial(
                show = {
                    Logger.v("initial show ${Thread.currentThread().name}")
                },
                hide = {
                    Logger.v("initial hide ${Thread.currentThread().name}")
                },
                onError = {
                    Logger.e("initial onError ${Thread.currentThread().name} $it")
                }
            ) {
                Logger.d("initial collect ${Thread.currentThread().name} $it")
            }
        }
    }

    fun refresh(view: View) {
        val result = mViewModel.getPagingResult()
        lifecycleScope.launch {
            result.refresh(
                show = {
                    Logger.v("refresh show ${Thread.currentThread().name}")
                },
                hide = {
                    Logger.v("refresh hide ${Thread.currentThread().name}")
                },
                onError = {
                    Logger.e("refresh onError ${Thread.currentThread().name} $it")
                }
            ) {
                Logger.d("refresh collect ${Thread.currentThread().name} $it")
            }
        }
    }

    fun loadAfter(view: View) {
        val result = mViewModel.getPagingResult()
        lifecycleScope.launch {
            result.after(
                onError = {
                    Logger.e("after onError ${Thread.currentThread().name} $it")
                }
            ) {
                Logger.d("after collect ${Thread.currentThread().name} $it")
            }
        }
    }

    fun loadBefore(view: View) {
        val result = mViewModel.getPagingResult()
        lifecycleScope.launch {
            result.before(
                onError = {
                    Logger.e("before onError ${Thread.currentThread().name} $it")
                }
            ) {
                Logger.d("before collect ${Thread.currentThread().name} $it")
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
