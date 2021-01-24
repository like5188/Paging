package com.like.paging.sample.paging.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.like.common.base.BaseDialogFragment
import com.like.paging.sample.R
import com.like.paging.sample.databinding.DialogfragmentProgressBinding

class ProgressDialogFragment private constructor() : BaseDialogFragment() {
    companion object {
        fun newInstance(): ProgressDialogFragment {
            return ProgressDialogFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<DialogfragmentProgressBinding>(
            inflater,
            R.layout.dialogfragment_progress,
            container,
            false
        )
        return binding.root
    }

}