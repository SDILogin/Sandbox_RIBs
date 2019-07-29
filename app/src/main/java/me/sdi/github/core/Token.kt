package me.sdi.github.core

import com.squareup.moshi.Json

data class Token(
    @Json(name = "id") val id: Long,
    @Json(name = "token") val token: String
)