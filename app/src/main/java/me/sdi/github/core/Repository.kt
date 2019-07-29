package me.sdi.github.core

import com.squareup.moshi.Json

data class Repository(
    @Json(name = "id") val id: Long,
    @Json(name = "name") val name: String,
    @Json(name = "private") val private: Boolean,
    @Json(name = "description") val description: String?,
    @Json(name = "stargazers_count") val stargazersCount: Int,
    @Json(name = "watchers_count") val watchersCount: Int
)