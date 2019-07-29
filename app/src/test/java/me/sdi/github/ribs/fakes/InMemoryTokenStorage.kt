package me.sdi.github.ribs.fakes

import me.sdi.github.core.Token
import me.sdi.github.ribs.TokenStorage

class InMemoryTokenStorage : TokenStorage {
    private var token: Token = TokenStorage.UNAUTHORIZED_USER_TOKEN

    override fun restore(): Token = token

    override fun store(token: Token) {
        this.token = token
    }

}
