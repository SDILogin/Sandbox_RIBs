package me.sdi.github.ribs.fakes

import me.sdi.github.core.Repository
import me.sdi.github.core.User
import me.sdi.github.network.UserInfoGateway

class FakeUserInfoGateway : UserInfoGateway {
    override fun currentUserInfo(): User = User(
        id = 1L,
        login = "valid_user_login@protonmail.com",
        avatar = "https://static.github.com/avatars/users/1/asf.jpg",
        url = "https://github.com/valid_user_profile",
        name = "Joe",
        company = "AwesomeCompany",
        location = "California",
        numberOfPublicRepos = 42,
        numberOfPublicGists = 142,
        numberOfFollowers = 242,
        numberOfFollowingUsers = 342
    )

    override fun repositories(): List<Repository> = listOf(
        Repository(
            id = 1001L,
            name = "My first app",
            private = false,
            description = "My very first app",
            stargazersCount = 1,
            watchersCount = 0
        ),
        Repository(
            id = 1002L,
            name = "My very popular app",
            private = false,
            description = "My awesome lib which is used by thousands developers",
            stargazersCount = 1000,
            watchersCount = 303
        ),
        Repository(
            id = 1003L,
            name = "My private app",
            private = true,
            description = "My private app",
            stargazersCount = 0,
            watchersCount = 0
        )
    )
}
