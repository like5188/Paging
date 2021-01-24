package com.like.paging.sample.paging.util

import com.like.paging.sample.data.db.Db
import com.like.paging.sample.paging.dataSource.inDb.ArticlePagingDbDataSource
import com.like.paging.sample.paging.dataSource.inDb.BannerDbDataSource
import com.like.paging.sample.paging.dataSource.inDb.MergePagingDbDataSource
import com.like.paging.sample.paging.dataSource.inDb.TopArticleDbDataSource
import com.like.paging.sample.paging.dataSource.inMemory.ArticlePagingDataSource
import com.like.paging.sample.paging.dataSource.inMemory.BannerDataSource
import com.like.paging.sample.paging.dataSource.inMemory.MergePagingDataSource
import com.like.paging.sample.paging.dataSource.inMemory.TopArticleDataSource
import com.like.paging.sample.paging.repository.PagingRepository
import com.like.paging.sample.paging.viewModel.PagingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val myModule = module {
    //Dao
    single {
        Db.getInstance(get()).bannerEntityDao()
    }
    single {
        Db.getInstance(get()).topArticleEntityDao()
    }
    single {
        Db.getInstance(get()).articleEntityDao()
    }

    //DataSource
    factory {
        BannerDbDataSource(get(), get())
    }
    factory {
        TopArticleDbDataSource(get(), get())
    }
    factory {
        ArticlePagingDbDataSource(get(), get())
    }
    factory {
        MergePagingDbDataSource(get(), get(), get())
    }
    factory {
        BannerDataSource()
    }
    factory {
        TopArticleDataSource()
    }
    factory {
        ArticlePagingDataSource()
    }
    factory {
        MergePagingDataSource(get(), get(), get())
    }

    //Repository
    factory {
        PagingRepository(get(), get(), get(), get(), get(), get(), get(), get())
    }

    //viewModel
    viewModel {
        PagingViewModel(get())
    }
}