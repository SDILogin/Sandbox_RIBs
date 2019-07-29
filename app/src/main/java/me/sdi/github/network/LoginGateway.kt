package me.sdi.github.network

import android.util.Base64
import com.squareup.moshi.Moshi
import me.sdi.github.core.Token
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import timber.log.Timber

interface LoginGateway {
    fun performLoginOrThrowError(login: String, password: String): Token
}

class GithubLoginGateway(
    private val okHttpClient: OkHttpClient,
    private val moshi: Moshi
): LoginGateway {

    override fun performLoginOrThrowError(login: String, password: String): Token {
        val base64String = Base64.encodeToString("$login:$password".toByteArray(), Base64.DEFAULT)
            .replace("\n", "")
        val request = Request.Builder()
            .url("https://api.github.com/authorizations")
            .header("Authorization", "Basic $base64String")
            .header("Content-Type", "application/json")
            .post(
                RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"),
                    """{
                        "scopes": ["public_repo"],
                        "note": "test note"
                    }""".trimIndent()
                )
            )
            .build()

        val response = okHttpClient.newCall(request).execute()
        val jsonResponse = response.body()?.string()
        jsonResponse?.let {
            Timber.d(jsonResponse)
            val tokenAdapter = moshi.adapter(Token::class.java)
            val token = tokenAdapter.fromJson(jsonResponse)
            return if (token != null) token else throw IllegalArgumentException("Failed to get token")
        }

        throw IllegalArgumentException("Failed to get token")
    }
}