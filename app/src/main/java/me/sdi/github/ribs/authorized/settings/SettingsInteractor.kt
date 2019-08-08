package me.sdi.github.ribs.authorized.settings

import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.RibInteractor
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import me.sdi.github.RxSchedulers
import me.sdi.github.ribs.TokenStorage
import me.sdi.github.ribs.TokenStorage.Companion.UNAUTHORIZED_USER_TOKEN
import javax.inject.Inject

/**
 * Coordinates Business Logic for [SettingsScope].
 *
 * TODO describe the logic of this scope.
 */
@RibInteractor
class SettingsInteractor : Interactor<SettingsInteractor.SettingsPresenter, SettingsRouter>() {

    @Inject
    lateinit var presenter: SettingsPresenter

    @Inject
    lateinit var tokenStorage: TokenStorage

    @Inject
    lateinit var rxSchedulers: RxSchedulers

    @Inject
    lateinit var listener: Listener

    lateinit var logoutDisposable: Disposable

    /**
     * Presenter interface implemented by this RIB's view.
     */
    interface SettingsPresenter {
        fun logoutRequests(): Observable<Unit>
    }

    interface Listener {
        fun onLogout()
    }

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)

        logoutDisposable = presenter.logoutRequests()
            .observeOn(rxSchedulers.io())
            .doOnNext { performLogout() }
            .onErrorReturn { Unit }
            .observeOn(rxSchedulers.main())
            .subscribe { unit -> listener.onLogout() }
    }

    override fun willResignActive() {
        super.willResignActive()

        logoutDisposable.dispose()
    }

    private fun performLogout() {
        tokenStorage.store(UNAUTHORIZED_USER_TOKEN)
    }
}
