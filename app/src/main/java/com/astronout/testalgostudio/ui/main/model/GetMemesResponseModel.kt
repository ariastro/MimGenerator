package com.astronout.testalgostudio.ui.main.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
@JsonClass(generateAdapter = true)
data class GetMemesResponseModel(
    @Json(name = "data")
    val data: Data,
    @Json(name = "success")
    val success: Boolean
): Parcelable