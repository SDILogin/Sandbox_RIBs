package me.sdi.github.core

import com.squareup.moshi.Json

data class User(
    @Json(name = "id") val id: Long,
    @Json(name = "login") val login: String,
    @Json(name = "avatar_url") val avatar: String?,
    @Json(name = "html_url") val url: String,
    @Json(name = "name") val name: String,
    @Json(name = "company") val company: String?,
    @Json(name = "location") val location: String?,
    @Json(name = "public_repos") val numberOfPublicRepos: Int,
    @Json(name = "public_gists") val numberOfPublicGists: Int,
    @Json(name = "followers") val numberOfFollowers: Int,
    @Json(name = "following") val numberOfFollowingUsers: Int
)
