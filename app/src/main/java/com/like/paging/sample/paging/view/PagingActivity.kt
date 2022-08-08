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
import com.like.paging.sample.data.model.ArticleEntity
import com.like.paging.sample.databinding.ActivityPagingBinding
import com.like.paging.sample.paging.viewModel.PagingViewModel
import com.like.paging.util.PagingResultCollector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PagingActivity : AppCompatActivity() {
    private val mBinding by lazy {
        DataBindingUtil.setContentView<ActivityPagingBinding>(this, R.layout.activity_paging)
    }
    private val mViewModel: PagingViewModel by viewModel()
    private val pagingResultCollector = PagingResultCollector<ArticleEntity>()
    private val callback = object : PagingResultCollector.Callback<ArticleEntity> {
        override fun onShow() {
            Logger.v("onShow ${Thread.currentThread().name}")
        }

        override fun onHide() {
            Logger.v("onHide ${Thread.currentThread().name}")
        }

        override suspend fun onError(requestType: RequestType, throwable: Throwable) {
            Logger.e("onError $requestType ${Thread.currentThread().name} $throwable")
        }

        override suspend fun onSuccess(requestType: RequestType, list: List<ArticleEntity>?) {
            Logger.d("onSuccess $requestType ${Thread.currentThread().name} $list")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding
        lifecycleScope.launch {
            pagingResultCollector.collectFrom(mViewModel.getPagingResult(), callback)
        }
    }

    fun refresh(view: View) {
        lifecycleScope.launch {
            pagingResultCollector.refresh()
        }
    }

    fun loadAfter(view: View) {
        lifecycleScope.launch {
            pagingResultCollector.after()
        }
    }

    fun loadBefore(view: View) {
        lifecycleScope.launch {
            pagingResultCollector.before()
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
