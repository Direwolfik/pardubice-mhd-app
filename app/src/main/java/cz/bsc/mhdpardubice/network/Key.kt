package cz.bsc.mhdpardubice.network


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Key(
    @Json(name = "key")
    val key: String? = ""
)