package me.sdi.github.ribs.unauthorized

import com.uber.rib.core.InteractorHelper
import com.uber.rib.core.RibTestBasePlaceholder
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import me.sdi.github.RxSchedulers
import me.sdi.github.core.Token
import me.sdi.github.network.LoginGateway
import me.sdi.github.ribs.TokenStorage
import me.sdi.github.ribs.fakes.InMemoryTokenStorage
import me.sdi.github.ribs.fakes.TestRxSchedulers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class LoginInteractorTest : RibTestBasePlaceholder() {

    @Mock
    internal lateinit var router: LoginRouter

    private lateinit var presenter: FakeLoginPresenter
    private lateinit var loginInteractorListener: LoginInteractorListener
    private lateinit var loginGateway: LoginGateway
    private lateinit var tokenStorage: TokenStorage
    private lateinit var rxSchedulers: RxSchedulers
    private lateinit var interactor: LoginInteractor

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        presenter = FakeLoginPresenter()
        loginInteractorListener = LoginInteractorListener()
        loginGateway = FakeLoginGateway()
        tokenStorage = InMemoryTokenStorage()
        rxSchedulers = TestRxSchedulers()
        interactor = TestLoginInteractor.create(
            presenter,
            loginInteractorListener,
            loginGateway,
            tokenStorage,
            rxSchedulers
        )
    }

    /**
     * notify listener about successful login
     */
    @Test
    fun when_login_successful_invoke_callback() {
        // do
        InteractorHelper.attach(interactor, presenter, router, null)
        presenter.onUserTryToLoginWithCredentials("valid_user_email@protonmail.com", "123456")
        InteractorHelper.detach(interactor)

        // verify
        assertThat(loginInteractorListener.loggedIn, `is`(true))
    }

    /**
     * show error when login failed
     */
    @Test
    fun when_login_with_invalid_credentials_show_error() {
        // do
        InteractorHelper.attach(interactor, presenter, router, null)
        presenter.onUserTryToLoginWithCredentials("invalid_email@gmail.com", "123456")
        InteractorHelper.detach(interactor)

        // verify
        assertThat(presenter.isLoginFailedMessageShown(), `is`(true))
    }

    class FakeLoginPresenter: LoginInteractor.LoginPresenter {

        private var loginFailedMessageShown = false
        private val loginCredentialsSubject = BehaviorSubject.create<Pair<String, String>>()

        override fun loginClicks(): Observable<Pair<String, String>> = loginCredentialsSubject.hide()

        override fun showLoginFailedMessage() {
            loginFailedMessageShown = true
        }

        fun onUserTryToLoginWithCredentials(email: String, password: String) {
            loginCredentialsSubject.onNext(Pair(email, password))
        }

        fun isLoginFailedMessageShown() = loginFailedMessageShown
    }

    class FakeLoginGateway : LoginGateway {

        private val validLogin = "valid_user_email@protonmail.com"
        private val validPassword = "123456"
        private val validUserToken = Token(1L, "123da44")

        override fun performLoginOrThrowError(login: String, password: String): Token {
            return if (login == validLogin && password == validPassword) {
                validUserToken
            } else {
                throw IllegalArgumentException("Invalid credentials. Login: $login Password: $password")
            }
        }
    }

    class LoginInteractorListener : LoginInteractor.Listener {

        var loggedIn = false

        override fun onSuccessfulLogin() {
            loggedIn = true
        }
    }
}