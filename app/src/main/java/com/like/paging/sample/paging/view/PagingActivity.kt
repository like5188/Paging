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

        lifecycleScope.launch {
            mViewModel.getPagingResult().collect(
                show = {
                    Logger.v("show ${Thread.currentThread().name}")
                },
                hide = {
                    Logger.v("hide ${Thread.currentThread().name}")
                },
                onError = { requestType, throwable ->
                    Logger.e("onError ${Thread.currentThread().name} requestType=$requestType throwable=$throwable")
                }
            ) { requestType, list ->
                Logger.d("onSuccess ${Thread.currentThread().name} requestType=$requestType data=$list")
            }
        }
    }

    fun initial(view: View) {
        lifecycleScope.launch {
            mViewModel.getPagingResult().initial()
        }
    }

    fun refresh(view: View) {
        lifecycleScope.launch {
            mViewModel.getPagingResult().refresh()
        }
    }

    fun loadAfter(view: View) {
        lifecycleScope.launch {
            mViewModel.getPagingResult().loadAfter?.invoke()
        }
    }

    fun loadBefore(view: View) {
        lifecycleScope.launch {
            mViewModel.getPagingResult().loadBefore?.invoke()
        }
    }

    fun retry(view: View) {
        lifecycleScope.launch {
            mViewModel.getPagingResult().retry()
        }
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
