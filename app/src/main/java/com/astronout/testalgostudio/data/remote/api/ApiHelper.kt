package com.astronout.testalgostudio.data.remote.api

import com.astronout.testalgostudio.ui.main.model.GetMemesResponseModel
import retrofit2.Response

interface ApiHelper {

    suspend fun getMemes(): Response<GetMemesResponseModel>

}