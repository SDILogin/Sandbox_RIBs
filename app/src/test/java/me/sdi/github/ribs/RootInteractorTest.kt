package me.sdi.github.ribs

import com.uber.rib.core.InteractorHelper
import com.uber.rib.core.RibTestBasePlaceholder
import me.sdi.github.core.Token
import me.sdi.github.ribs.fakes.InMemoryTokenStorage
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.inOrder
import org.mockito.MockitoAnnotations

class RootInteractorTest : RibTestBasePlaceholder() {

    @Mock
    internal lateinit var presenter: RootInteractor.RootPresenter
    @Mock
    internal lateinit var router: RootRouter

    private lateinit var tokenStorage: TokenStorage
    private lateinit var interactor: RootInteractor

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        tokenStorage = InMemoryTokenStorage()

        interactor = TestRootInteractor.create(presenter, tokenStorage)
    }

    /**
     * authorized user (with non default token in storage) redirected to the
     * dashboard screen
     */
    @Test
    fun when_user_authorized_show_dashboard() {
        // preconditions
        tokenStorage.store(Token(1L, "12dd44asd44"))

        // do
        InteractorHelper.attach(interactor, presenter, router, null)
        InteractorHelper.detach(interactor)

        // verify
        val routerInvocationOrder = inOrder(router)
        routerInvocationOrder.verify(router).attachDashboardView()
        routerInvocationOrder.verify(router).detachLoginView()
    }

    /**
     * unauthorized user redirected to the login screen when application started
     */
    @Test
    fun when_user_unauthorized_show_login_screen() {
        // preconditions
        tokenStorage.store(TokenStorage.UNAUTHORIZED_USER_TOKEN)

        // do
        InteractorHelper.attach(interactor, presenter, router, null)
        InteractorHelper.detach(interactor)

        // verify
        val routerInvocationOrder = inOrder(router)
        routerInvocationOrder.verify(router).attachLoginView()
        routerInvocationOrder.verify(router).detachDashboardView()
    }

    /**
     * user redirected to the dashboard screen after successful login
     */
    @Test
    fun when_triggered_successful_login_callback_method_show_dashboard_screen() {
        // do
        InteractorHelper.attach(interactor, presenter, router, null)
        interactor.onSuccessfulLogin()
        InteractorHelper.detach(interactor)

        // verify
        val routerInvocationOrder = inOrder(router)
        routerInvocationOrder.verify(router).attachDashboardView()
        routerInvocationOrder.verify(router).detachLoginView()
    }
}

