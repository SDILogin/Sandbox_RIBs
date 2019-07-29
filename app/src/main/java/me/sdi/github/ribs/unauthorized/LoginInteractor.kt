package me.sdi.github.ribs.unauthorized

import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.RibInteractor
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import me.sdi.github.RxSchedulers
import me.sdi.github.network.LoginGateway
import me.sdi.github.ribs.TokenStorage
import timber.log.Timber
import javax.inject.Inject

/**
 * This interactor listens login credentials. When login credentials
 * received it must check them and notify presenter about login error.
 * In case of successful login [LoginInteractor.Listener.onSuccessfulLogin]
 * will be called.
 */
@RibInteractor
class LoginInteractor : Interactor<LoginInteractor.LoginPresenter, LoginRouter>() {

    /**
     * Presenter interface implemented by this RIB's view.
     */
    interface LoginPresenter {
        fun loginClicks(): Observable<Pair<String, String>>
        fun showLoginFailedMessage()
    }

    interface Listener {
        fun onSuccessfulLogin()
    }

    @Inject
    lateinit var presenter: LoginPresenter

    @Inject
    lateinit var listener: Listener

    @Inject
    lateinit var loginGateway: LoginGateway

    @Inject
    lateinit var tokenStorage: TokenStorage

    @Inject
    lateinit var rxSchedulers: RxSchedulers

    lateinit var loginClicksSubscription: Disposable

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)

        loginClicksSubscription = presenter.loginClicks()
            .observeOn(rxSchedulers.io())
            .map {
                val email = it.first
                val password = it.second
                performLogin(email, password)
            }
            .observeOn(rxSchedulers.main())
            .subscribe(this::handleLoginResult, this::handleLoginError)
    }

    override fun willResignActive() {
        super.willResignActive()

        loginClicksSubscription.dispose()
    }

    private fun handleLoginResult(success: Boolean) {
        if (success) {
            Timber.d("Login success")
            listener.onSuccessfulLogin()
        } else {
            presenter.showLoginFailedMessage()
            Timber.d("Login failure")
        }
    }

    private fun handleLoginError(error: Throwable) {
        presenter.showLoginFailedMessage()
        Timber.w(error, "Failed to login")
    }

    private fun performLogin(email: String, password: String): Boolean {
        try {
            val token = loginGateway.performLoginOrThrowError(email, password)
            tokenStorage.store(token)
            return true
        } catch (exception: Throwable) {
            Timber.w(exception)
        }
        return false
    }
}
