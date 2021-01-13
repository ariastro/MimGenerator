package com.astronout.testalgostudio.data.remote.api

import com.astronout.testalgostudio.ui.main.model.GetMemesResponseModel
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("get_memes")
    suspend fun getMemes() : Response<GetMemesResponseModel>

}