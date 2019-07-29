package me.sdi.github.ribs

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import me.sdi.github.core.Token
import me.sdi.github.ribs.TokenStorage.Companion.UNAUTHORIZED_USER_TOKEN

const val SHARED_PREF_NAME = "SharedPreferences"
const val SHARED_PREF_KEY_TOKEN_ID = "id"
const val SHARED_PREF_KEY_TOKEN = "token"

class SharePreferencesTokenStorage(context: Context): TokenStorage {

    private val sharedPreferences: SharedPreferences

    override fun store(token: Token) {
        sharedPreferences.edit()
            .putLong(SHARED_PREF_KEY_TOKEN_ID, token.id)
            .putString(SHARED_PREF_KEY_TOKEN, token.token)
            .apply()
    }

    override fun restore(): Token {
        val tokenId = sharedPreferences.getLong(SHARED_PREF_KEY_TOKEN_ID, UNAUTHORIZED_USER_TOKEN.id)
        val token = sharedPreferences.getString(SHARED_PREF_KEY_TOKEN, UNAUTHORIZED_USER_TOKEN.token)
        return Token(tokenId, token!!)
    }

    init {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)
    }
}

interface TokenStorage {
    companion object {
        @JvmField
        val UNAUTHORIZED_USER_TOKEN = Token(-1, "")
    }

    fun store(token: Token)
    fun restore(): Token
}