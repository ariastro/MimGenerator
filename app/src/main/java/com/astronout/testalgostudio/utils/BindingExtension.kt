package com.astronout.testalgostudio.utils

import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.astronout.testalgostudio.R
import com.astronout.testalgostudio.utils.glide.GlideApp
import com.bumptech.glide.GenericTransitionOptions
import de.hdodenhof.circleimageview.CircleImageView

@BindingAdapter("setImageUrl")
fun AppCompatImageView.setImageUrl(imageUrl: String?) {
    GlideApp.with(context)
        .load(imageUrl)
        .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
        .into(this)
}

@BindingAdapter("setCircleImageUrl")
fun CircleImageView.setCircleImageUrl(imageUrl: String?) {
    GlideApp.with(context)
        .load(imageUrl)
        .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
        .into(this)
}

@BindingAdapter("binding")
fun bindAppCompatEditText(appCompatEditText: AppCompatEditText, string: MutableLiveData<String>){
    val pair: Pair<MutableLiveData<String>, TextWatcherAdapter>? = appCompatEditText.getTag(R.id.bound_observable) as Pair<MutableLiveData<String>, TextWatcherAdapter>?
    if (pair==null || pair.first != string) {
        if (pair != null) {
            appCompatEditText.removeTextChangedListener(pair.second)
        }
        val watcher = object : TextWatcherAdapter(){
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                super.onTextChanged(p0, p1, p2, p3)
                string.value = p0?.toString()
            }
        }
        appCompatEditText.setTag(R.id.bound_observable, Pair(string, watcher))
        appCompatEditText.addTextChangedListener(watcher)
    }
    val newValue = string.value
    if (appCompatEditText.text.toString() != newValue){
        appCompatEditText.setText(newValue)
    }
}