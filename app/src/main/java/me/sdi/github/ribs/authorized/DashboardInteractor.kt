package me.sdi.github.ribs.authorized

import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.RibInteractor
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import timber.log.Timber
import javax.inject.Inject

/**
 * This interactor responsible for displaying screen based on selected item
 * in bottom navigation bar at the dashboard. When user tap on some item
 * on dashboard's navigation bar this class will handle request
 */
@RibInteractor
class DashboardInteractor : Interactor<DashboardInteractor.DashboardPresenter, DashboardRouter>() {

    @Inject
    lateinit var presenter: DashboardPresenter

    lateinit var mNavigationItemSelectionDisposable: Disposable

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)

        // TODO: Add attachment logic here (RxSubscriptions, etc.).
        mNavigationItemSelectionDisposable = presenter.navigationItemSelected()
            .startWith(DashboardPresenter.Screen.MAIN)
            .subscribe(
                { screen ->
                    when(screen) {
                        DashboardPresenter.Screen.MAIN -> router.showMainScreen()
                        DashboardPresenter.Screen.CURRENT_USER_REPOSITORIES -> router.showCurrentUserProjects()
                        DashboardPresenter.Screen.SETTINGS -> router.showSettingsScreen()
                        else -> Timber.w("failed to show $screen")
                    }
                },
                { error -> Timber.w(error) }
            )
    }

    override fun willResignActive() {
        super.willResignActive()

        mNavigationItemSelectionDisposable.dispose()
    }

    interface DashboardPresenter {
        enum class Screen {
            MAIN, CURRENT_USER_REPOSITORIES, SETTINGS
        }

        fun navigationItemSelected(): Observable<Screen>
    }
}
