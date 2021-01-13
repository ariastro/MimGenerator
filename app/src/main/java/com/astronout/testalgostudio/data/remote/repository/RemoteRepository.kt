package com.astronout.testalgostudio.data.remote.repository

import com.astronout.testalgostudio.data.remote.api.ApiHelper

class RemoteRepository(private val apiHelper: ApiHelper) {

    suspend fun getMemes() = apiHelper.getMemes()

}