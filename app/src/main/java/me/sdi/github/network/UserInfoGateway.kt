package me.sdi.github.network

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import me.sdi.github.core.Repository
import me.sdi.github.core.User
import me.sdi.github.ribs.TokenStorage
import okhttp3.OkHttpClient
import okhttp3.Request
import timber.log.Timber
import java.lang.reflect.Type


interface UserInfoGateway {
    fun currentUserInfo(): User
    fun repositories(): List<Repository>
}

class GithubUserInfoGateway(
    private val okHttpClient: OkHttpClient,
    private val tokenStorage: TokenStorage,
    private val moshi: Moshi
) : UserInfoGateway {

    override fun currentUserInfo(): User {
        val token = tokenStorage.restore().token
        val request = Request.Builder()
            .header("Authorization", "token $token")
            .header("Content-Type", "application/json")
            .url("https://api.github.com/user")
            .get()
            .build()

        val response = okHttpClient.newCall(request).execute()
        val jsonResponse = response.body()?.string()
        jsonResponse?.let {
            Timber.d(jsonResponse)
            val userAdapter = moshi.adapter(User::class.java)
            val user = userAdapter.fromJson(jsonResponse)
            return if (user != null) user else throw IllegalArgumentException("Failed to get user info")
        }

        throw IllegalArgumentException("Failed to get user info")
    }

    override fun repositories(): List<Repository> {
        val token = tokenStorage.restore().token
        val request = Request.Builder()
            .header("Authorization", "token $token")
            .header("Content-Type", "application/json")
            .url("https://api.github.com/user/repos")
            .get()
            .build()

        val response = okHttpClient.newCall(request).execute()
        val jsonResponse = response.body()?.string()
        jsonResponse?.let {
            Timber.d(jsonResponse)
            val type: Type = Types.newParameterizedType(List::class.java, Repository::class.java)
            val moshiAdapter: JsonAdapter<List<Repository>> = moshi.adapter(type)
            val userRepositories = moshiAdapter.fromJson(jsonResponse)
            if (userRepositories != null) {
                return userRepositories
            } else {
                throw IllegalArgumentException("Failed to get user repositories")
            }
        }

        throw IllegalArgumentException("Failed to get user repositories")
    }
}