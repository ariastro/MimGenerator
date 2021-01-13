package com.astronout.testalgostudio.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astronout.testalgostudio.data.remote.repository.RemoteRepository
import com.astronout.testalgostudio.ui.main.model.GetMemesResponseModel
import com.astronout.testalgostudio.utils.Constants.NO_INTERNET_CONNECTION
import com.astronout.testalgostudio.utils.NetworkHelper
import com.astronout.testalgostudio.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: RemoteRepository, private val networkHelper: NetworkHelper): ViewModel() {

    private val _memeList = MutableLiveData<Resource<GetMemesResponseModel>>()
    val memeList: LiveData<Resource<GetMemesResponseModel>>
        get() = _memeList

    init {
        getMemes()
    }

    fun getMemes() {
        viewModelScope.launch(Dispatchers.IO) {
            _memeList.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                repository.getMemes().let {
                    if (it.isSuccessful) {
                        _memeList.postValue(Resource.success(it.body()))
                    } else {
                        _memeList.postValue(Resource.error(it.errorBody().toString(), null))
                    }
                }
            } else {
                _memeList.postValue(Resource.error(NO_INTERNET_CONNECTION, null))
            }
        }
    }

}