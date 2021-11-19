package com.like.paging.sample

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.like.paging.sample.paging.view.PagingActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun startPagingActivity(view: View) {
        startActivity(Intent(this, PagingActivity::class.java))
    }

}
