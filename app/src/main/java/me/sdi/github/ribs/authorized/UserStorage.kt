package me.sdi.github.ribs.authorized

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.squareup.moshi.JsonAdapter
import me.sdi.github.core.User

const val SHARED_PREF_NAME = "SharedPreferences"
const val SHARED_PREF_KEY_USER_JSON = "user_json"

interface UserStorage {
    fun store(user: User)
    fun restore(): User?
}

class SharedPreferencesUserStorage(
    context: Context,
    private val userJsonAdapter: JsonAdapter<User>
) : UserStorage {

    private val sharedPreferences: SharedPreferences

    override fun store(user: User) {
        val userAsJsonString = userJsonAdapter.toJson(user)
        sharedPreferences.edit()
            .putString(SHARED_PREF_KEY_USER_JSON, userAsJsonString)
            .apply()
    }

    override fun restore(): User? {
        val userJsonString = sharedPreferences.getString(SHARED_PREF_KEY_USER_JSON, null)
        return if (userJsonString != null) userJsonAdapter.fromJson(userJsonString) else null
    }

    init {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)
    }
}