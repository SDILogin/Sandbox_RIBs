package me.sdi.github.ribs.authorized.main

import com.uber.rib.core.InteractorHelper
import com.uber.rib.core.RibTestBasePlaceholder
import me.sdi.github.core.Repository
import me.sdi.github.core.User
import me.sdi.github.network.UserInfoGateway
import me.sdi.github.ribs.authorized.UserStorage
import me.sdi.github.ribs.fakes.FakeUserInfoGateway
import me.sdi.github.ribs.fakes.TestRxSchedulers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class MainInteractorTest : RibTestBasePlaceholder() {

    @Mock
    internal lateinit var router: MainRouter

    private lateinit var presenter: FakeMainPresenter
    private lateinit var userInfoGateway: UserInfoGateway
    private lateinit var userStorage: UserStorage
    private lateinit var interactor: MainInteractor

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        presenter = FakeMainPresenter()
        userInfoGateway = FakeUserInfoGateway()
        userStorage = FakeUserStorage()
        interactor = TestMainInteractor.create(presenter, userInfoGateway, userStorage, TestRxSchedulers())
    }

    /**
     * show user profile data when user opens main screen tab on dashboard
     */
    @Test
    fun display_user_data_on_start() {
        // do
        InteractorHelper.attach(interactor, presenter, router, null)
        InteractorHelper.detach(interactor)

        // verify
        val displayedUserData = presenter.getDispalayedUserData()
        assertThat(displayedUserData, `is`(equalTo(User(
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
        ))))
    }

    class FakeMainPresenter: MainInteractor.MainPresenter {
        private var progressBarShown = false
        private var dispalayedUserData: User = User(
            id = -1L,
            login = "",
            avatar = "",
            url = "",
            name = "",
            company = "",
            location = "",
            numberOfPublicRepos = -1,
            numberOfPublicGists = -1,
            numberOfFollowers = -1,
            numberOfFollowingUsers = -1
        )

        fun getDispalayedUserData() = dispalayedUserData

        override fun showUserId(id: Long) {
            dispalayedUserData = dispalayedUserData.copy(id = id)
        }

        override fun showLogin(login: String) {
            dispalayedUserData = dispalayedUserData.copy(login = login)
        }

        override fun showAvatar(avatarUrl: String) {
            dispalayedUserData = dispalayedUserData.copy(avatar = avatarUrl)
        }

        override fun hideAvatar() {
            dispalayedUserData = dispalayedUserData.copy(avatar = "")
        }

        override fun showGithubPageUrl(githubPageUrl: String) {
            dispalayedUserData = dispalayedUserData.copy(url = githubPageUrl)
        }

        override fun showUserName(userName: String) {
            dispalayedUserData = dispalayedUserData.copy(name = userName)
        }

        override fun showCompany(company: String) {
            dispalayedUserData = dispalayedUserData.copy(company = company)
        }

        override fun hideCompany() {
            dispalayedUserData = dispalayedUserData.copy(company = "")
        }

        override fun showLocation(location: String) {
            dispalayedUserData = dispalayedUserData.copy(location = location)
        }

        override fun hideLocation() {
            dispalayedUserData = dispalayedUserData.copy(location = "")
        }

        override fun showNumberOfPublicRepos(numberOfPublicRepos: Int) {
            dispalayedUserData = dispalayedUserData.copy(numberOfPublicRepos = numberOfPublicRepos)
        }

        override fun showNumberOfPublicGists(numberOfPublicGists: Int) {
            dispalayedUserData = dispalayedUserData.copy(numberOfPublicGists = numberOfPublicGists)
        }

        override fun showNumberOfFollowers(numberOfFollowers: Int) {
            dispalayedUserData = dispalayedUserData.copy(numberOfFollowers = numberOfFollowers)
        }

        override fun showNumberOfFollowingUsers(numberOfFollowingUsers: Int) {
            dispalayedUserData = dispalayedUserData.copy(numberOfFollowingUsers= numberOfFollowingUsers)
        }

        override fun showProgressBar() {
            progressBarShown = true
        }

        override fun hideProgressBar() {
            progressBarShown = false
        }
    }


    class FakeUserStorage : UserStorage {

        private var user: User? = null

        override fun store(user: User) {
            this.user = user
        }

        override fun restore(): User? = user
    }
}