package com.astronout.testalgostudio.ui.main.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
@JsonClass(generateAdapter = true)
data class Meme(
    @Json(name = "box_count")
    val boxCount: Int,
    @Json(name = "height")
    val height: Int,
    @Json(name = "id")
    val id: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "url")
    val url: String,
    @Json(name = "width")
    val width: Int
): Parcelable