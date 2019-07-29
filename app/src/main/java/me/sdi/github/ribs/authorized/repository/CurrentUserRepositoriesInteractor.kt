package me.sdi.github.ribs.authorized.repository

import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.RibInteractor
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import me.sdi.github.RxSchedulers
import me.sdi.github.core.Repository
import me.sdi.github.network.UserInfoGateway
import timber.log.Timber
import javax.inject.Inject

/**
 * Interactor which controls repositories screen. Responsibility of this
 * class is loading user repositories and controlling screen state via
 * handling remote server response. This class will notify presenter
 * about loaded data
 */
@RibInteractor
class CurrentUserRepositoriesInteractor :
    Interactor<CurrentUserRepositoriesInteractor.CurrentUserRepositoriesPresenter, CurrentUserRepositoriesRouter>() {

    @Inject
    lateinit var presenter: CurrentUserRepositoriesPresenter

    @Inject
    lateinit var userInfoGateway: UserInfoGateway

    @Inject
    lateinit var rxScedulers: RxSchedulers

    lateinit var fetchUserRepositoriesDisposable: Disposable

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)

        fetchUserRepositoriesDisposable = Single
            .fromCallable { userInfoGateway.repositories() }
            .subscribeOn(rxScedulers.io())
            .observeOn(rxScedulers.main())
            .doOnSubscribe { presenter.showProgressBar() }
            .doAfterTerminate{ presenter.hideProgressBar() }
            .subscribe(this::showRepositories, this::handleError)
    }

    override fun willResignActive() {
        super.willResignActive()

        fetchUserRepositoriesDisposable.dispose()
    }

    private fun showRepositories(repositories: List<Repository>) {
        if (repositories.isNotEmpty()) {
            presenter.hideEmptyScreenPlaceholder()
            presenter.showRepositories(repositories)
        } else {
            presenter.showEmptyScreenPlaceholder()
        }
    }

    private fun handleError(error: Throwable) {
        Timber.w(error)
        presenter.showEmptyScreenPlaceholder()
    }

    interface CurrentUserRepositoriesPresenter {
        fun showProgressBar()
        fun hideProgressBar()
        fun showRepositories(repositories: List<Repository>)
        fun showEmptyScreenPlaceholder()
        fun hideEmptyScreenPlaceholder()
    }
}
