package com.astronout.testalgostudio.ui.detail.view

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.astronout.testalgostudio.R
import com.astronout.testalgostudio.base.BaseActivity
import com.astronout.testalgostudio.databinding.ActivityDetailBinding
import com.astronout.testalgostudio.ui.main.model.Meme
import com.astronout.testalgostudio.utils.glide.GlideApp
import com.bumptech.glide.GenericTransitionOptions

class DetailActivity : BaseActivity() {

    private lateinit var binding: ActivityDetailBinding

    companion object {
        const val EXTRA_MEME = "EXTRA_MEME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        setSupportActionBar(binding.toolbar)

        val extraData = intent.getParcelableExtra<Meme>(EXTRA_MEME)!!

        GlideApp.with(this)
            .load(extraData.url)
            .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
            .into(binding.image)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}