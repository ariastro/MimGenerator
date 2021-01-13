package com.astronout.testalgostudio.ui.main.view

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.astronout.testalgostudio.R
import com.astronout.testalgostudio.base.BaseActivity
import com.astronout.testalgostudio.databinding.ActivityMainBinding
import com.astronout.testalgostudio.ui.main.adapter.MainAdapter
import com.astronout.testalgostudio.ui.main.viewmodel.MainViewModel
import com.astronout.testalgostudio.utils.showErrorToasty
import com.astronout.testalgostudio.vo.Status
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    private val viewModel: MainViewModel by viewModel()
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(binding.toolbar)

        observeMemes()

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getMemes()
            binding.swipeRefreshLayout.isRefreshing = false
        }

    }

    private fun observeMemes() {
        viewModel.memeList.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    if (progress.isShowing) progress.dismiss()
                    adapter = MainAdapter(MainAdapter.OnClickListener { meme ->

                    })
                    adapter.submitList(it.data?.data?.memes)
                    adapter.notifyDataSetChanged()
                    binding.rvMeme.setHasFixedSize(true)
                    binding.rvMeme.adapter = adapter
                }
                Status.LOADING -> {
                    if (progress.isShowing) progress.dismiss()
                    progress.show()
                }
                Status.ERROR -> {
                    if (progress.isShowing) progress.dismiss()
                    showErrorToasty(it.message.toString())
                }
            }
        })
    }

}