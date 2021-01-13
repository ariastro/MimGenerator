package com.astronout.testalgostudio.data.remote.api

import com.astronout.testalgostudio.ui.main.model.GetMemesResponseModel
import retrofit2.Response

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {

    override suspend fun getMemes(): Response<GetMemesResponseModel> =
        apiService.getMemes()

}