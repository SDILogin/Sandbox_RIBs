package me.sdi.github.ribs

import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.RibInteractor
import me.sdi.github.ribs.unauthorized.LoginInteractor
import javax.inject.Inject

/**
 * Created when application launched. The main responsibility is to show the first one
 * screen to the user. When application launched by authorized user, dashboard screen
 * will be shown. In opposite case user must enter valid credentials -- redirected
 * to the login screen.
 *
 * Client code doesn't need to call any methods. Authorization state checked when
 * interactor [didBecomeActive]
 */
@RibInteractor
class RootInteractor : Interactor<RootInteractor.RootPresenter, RootRouter>(), LoginInteractor.Listener {

    @Inject
    lateinit var rootPresenter: RootPresenter

    @Inject
    lateinit var tokenStorage: TokenStorage

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)

        rootPresenter.showProgressBar()
        if (isAuthorized()) {
            router.attachDashboardView()
            router.detachLoginView()
        } else {
            router.attachLoginView()
            router.detachDashboardView()
        }
        rootPresenter.hideProgressBar()
    }

    override fun onSuccessfulLogin() {
        router.attachDashboardView()
        router.detachLoginView()
    }

    /**
     * Presenter interface implemented by this RIB's view.
     */
    interface RootPresenter {
        fun showProgressBar()
        fun hideProgressBar()
    }

    private fun isAuthorized(): Boolean {
        // assume token can't be outdated
        return tokenStorage.restore() != TokenStorage.UNAUTHORIZED_USER_TOKEN
    }
}
