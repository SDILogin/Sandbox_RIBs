package me.sdi.github.ribs.authorized.main

import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.RibInteractor
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import me.sdi.github.RxSchedulers
import me.sdi.github.core.User
import me.sdi.github.network.UserInfoGateway
import me.sdi.github.ribs.authorized.UserStorage
import timber.log.Timber
import javax.inject.Inject

/**
 * This interactor loads user information notifies presenter about
 * loaded data. Current class controls main tab screen
 */
@RibInteractor
class MainInteractor : Interactor<MainInteractor.MainPresenter, MainRouter>() {

    @Inject
    lateinit var presenter: MainPresenter

    @Inject
    lateinit var userInfoGateway: UserInfoGateway

    @Inject
    lateinit var userStorage: UserStorage

    @Inject
    lateinit var rxSchedulers: RxSchedulers

    private lateinit var fetchCurrentUserInfoDisposable: Disposable

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)

        fetchCurrentUserInfoDisposable = Single
            .fromCallable { loadUserInfo() }
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.main())
            .doOnSubscribe { presenter.showProgressBar() }
            .doAfterTerminate{ presenter.hideProgressBar() }
            .subscribe(this::displayUserInfo, this::displayError)
    }

    override fun willResignActive() {
        super.willResignActive()

        fetchCurrentUserInfoDisposable.dispose()
    }

    /**
     * Presenter interface implemented by this RIB's view.
     */
    interface MainPresenter {
        fun showUserId(id: Long)
        fun showLogin(login: String)
        fun showAvatar(avatarUrl: String)
        fun hideAvatar()
        fun showGithubPageUrl(githubPageUrl: String)
        fun showUserName(userName: String)
        fun showCompany(company: String)
        fun hideCompany()
        fun showLocation(location: String)
        fun hideLocation()
        fun showNumberOfPublicRepos(numberOfPublicRepos: Int)
        fun showNumberOfPublicGists(numberOfPublicGists: Int)
        fun showNumberOfFollowers(numberOfFollowers: Int)
        fun showNumberOfFollowingUsers(numberOfFollowingUsers: Int)
        fun showProgressBar()
        fun hideProgressBar()
    }

    private fun loadUserInfo(): User {
        if (userStorage.restore() == null) {
            val user = userInfoGateway.currentUserInfo()
            userStorage.store(user)
        }

        return userStorage.restore()!!
    }

    private fun displayError(error: Throwable) = Timber.e(error)

    private fun displayUserInfo(user: User) {
        Timber.d("fetched user info")

        presenter.showUserId(user.id)
        presenter.showLogin(user.login)
        presenter.showGithubPageUrl(user.url)
        presenter.showUserName(user.name)
        presenter.showNumberOfPublicRepos(user.numberOfPublicRepos)
        presenter.showNumberOfPublicGists(user.numberOfPublicGists)
        presenter.showNumberOfFollowers(user.numberOfFollowers)
        presenter.showNumberOfFollowingUsers(user.numberOfFollowingUsers)

        if (user.avatar != null) {
            presenter.showAvatar(user.avatar)
        } else {
            presenter.hideAvatar()
        }

        if (user.company != null) {
            presenter.showCompany(user.company)
        } else {
            presenter.hideCompany()
        }

        if (user.location != null) {
            presenter.showLocation(user.location)
        } else {
            presenter.hideLocation()
        }
    }
}
