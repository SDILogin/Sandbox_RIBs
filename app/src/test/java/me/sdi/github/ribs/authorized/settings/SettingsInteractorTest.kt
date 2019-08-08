package me.sdi.github.ribs.authorized.settings

import com.uber.rib.core.InteractorHelper
import com.uber.rib.core.RibTestBasePlaceholder
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import me.sdi.github.core.Token
import me.sdi.github.ribs.TokenStorage
import me.sdi.github.ribs.fakes.InMemoryTokenStorage
import me.sdi.github.ribs.fakes.TestRxSchedulers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class SettingsInteractorTest : RibTestBasePlaceholder() {

    @Mock
    internal lateinit var router: SettingsRouter

    lateinit var presenter: FakeSettingsPresenter
    lateinit var tokenStorage: TokenStorage
    lateinit var settingsInteractorListener: FakeSettingsInteractorListener
    lateinit var interactor: SettingsInteractor

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        presenter = FakeSettingsPresenter()
        tokenStorage = InMemoryTokenStorage()
        settingsInteractorListener = FakeSettingsInteractorListener()
        interactor = TestSettingsInteractor.create(
            presenter,
            tokenStorage,
            TestRxSchedulers(),
            settingsInteractorListener
        )
    }

    @Test
    fun `clear token storage after logout`() {
        // preconditions
        loggedInUser()

        // do
        InteractorHelper.attach(interactor, presenter, router, null)
        presenter.emitLogoutEvent()
        InteractorHelper.detach(interactor)

        // assert
        assertThat(tokenStorage.restore(), `is`(TokenStorage.UNAUTHORIZED_USER_TOKEN))
    }

    @Test
    fun `notify listener on logout`() {
        // preconditions
        loggedInUser()

        // do
        InteractorHelper.attach(interactor, presenter, router, null)
        presenter.emitLogoutEvent()
        InteractorHelper.detach(interactor)

        // assert
        assertThat(settingsInteractorListener.loggedOut, `is`(true));
    }

    private fun loggedInUser() {
        tokenStorage.store(Token(1L, "33"))
    }

    class FakeSettingsInteractorListener : SettingsInteractor.Listener {

        var loggedOut = false

        override fun onLogout() {
            loggedOut = true
        }
    }

    class FakeSettingsPresenter : SettingsInteractor.SettingsPresenter {

        private val logoutRequestsSubject = PublishSubject.create<Unit>()

        override fun logoutRequests(): Observable<Unit> = logoutRequestsSubject.hide()

        fun emitLogoutEvent() = logoutRequestsSubject.onNext(Unit)
    }
}