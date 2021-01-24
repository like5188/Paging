package com.like.paging.sample

import android.app.Application
import com.like.paging.sample.paging.util.myModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin

class MyApplication : Application() {
    companion object {
        const val PAGE_SIZE = 20
        lateinit var sInstance: Application
    }

    override fun onCreate() {
        super.onCreate()
        sInstance = this
        startKoin {
            androidContext(this@MyApplication)
        }
        loadKoinModules(myModule)
    }
}