package com.like.paging.sample

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.like.common.util.Logger
import com.like.paging.sample.paging.view.PagingActivity
import com.like.paging.util.ConcurrencyHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val concurrencyHelper = ConcurrencyHelper()

        (0..99).forEach {
            lifecycleScope.launch {
                concurrencyHelper.cancelPreviousThenRun {
                    delay((it - 100L) * -1)
                    test(it)
                }
            }
        }
    }

    private suspend fun test(i: Int): Int {
        Logger.d("执行 $i")
        yield()
        return i
    }

    fun startPagingActivity(view: View) {
        startActivity(Intent(this, PagingActivity::class.java))
    }

}
