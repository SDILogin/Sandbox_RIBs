package me.sdi.github.ribs.authorized

import com.uber.rib.core.InteractorHelper
import com.uber.rib.core.RibTestBasePlaceholder
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.inOrder
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class DashboardInteractorTest : RibTestBasePlaceholder() {

    @Mock
    internal lateinit var router: DashboardRouter

    private lateinit var presenter: FakeDashboardPresenter
    private lateinit var interactor: DashboardInteractor

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        presenter = FakeDashboardPresenter()
        interactor = TestDashboardInteractor.create(presenter)
    }

    /**
     * main screen shown by defaul. It will change after user tap on navigation bar item
     */
    @Test
    fun show_main_screen_by_default() {
        // do
        InteractorHelper.attach(interactor, presenter, router, null)
        InteractorHelper.detach(interactor)

        // verify
        verify(router).showMainScreen()
    }

    /**
     * displayed selected screen on dashboard
     */
    @Test
    fun show_selected_screen() {
        // do
        InteractorHelper.attach(interactor, presenter, router, null)
        presenter.onSelectedNavigationItem(DashboardInteractor.DashboardPresenter.Screen.CURRENT_USER_REPOSITORIES)
        presenter.onSelectedNavigationItem(DashboardInteractor.DashboardPresenter.Screen.SETTINGS)
        presenter.onSelectedNavigationItem(DashboardInteractor.DashboardPresenter.Screen.MAIN)
        InteractorHelper.detach(interactor)

        // verify
        val screenChangeOrder = inOrder(router)
        screenChangeOrder.verify(router).showMainScreen()
        screenChangeOrder.verify(router).showCurrentUserProjects()
        screenChangeOrder.verify(router).showSettingsScreen()
        screenChangeOrder.verify(router).showMainScreen()
    }

    class FakeDashboardPresenter: DashboardInteractor.DashboardPresenter {

        private val navigationItemSubject = BehaviorSubject.create<DashboardInteractor.DashboardPresenter.Screen>()

        override fun navigationItemSelected(): Observable<DashboardInteractor.DashboardPresenter.Screen> =
            navigationItemSubject.hide()

        fun onSelectedNavigationItem(screen: DashboardInteractor.DashboardPresenter.Screen) {
            navigationItemSubject.onNext(screen)
        }
    }
}