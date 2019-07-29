package me.sdi.github.ribs

import com.uber.rib.core.ViewRouter
import me.sdi.github.ribs.authorized.DashboardBuilder
import me.sdi.github.ribs.authorized.DashboardRouter
import me.sdi.github.ribs.unauthorized.LoginBuilder
import me.sdi.github.ribs.unauthorized.LoginRouter

/**
 * Adds and removes children of {@link RootBuilder.RootScope}.
 *
 * TODO describe the possible child configurations of this scope.
 */
class RootRouter(
    view: RootView,
    interactor: RootInteractor,
    component: RootBuilder.Component,

    private val loginBuilder: LoginBuilder,
    private val dashboardBuilder: DashboardBuilder
) : ViewRouter<RootView, RootInteractor, RootBuilder.Component>(view, interactor, component) {

    private var loginRouter: LoginRouter? = null
    private var dashboardRouter: DashboardRouter? = null

    fun attachLoginView() {
        loginRouter = loginBuilder.build(view).also {
            attachChild(it)
            view.addView(it.view)
        }
    }

    fun detachLoginView() {
        loginRouter?.let {
            detachChild(it)
            view.removeView(it.view)
            loginRouter = null
        }
    }

    fun attachDashboardView() {
        dashboardRouter = dashboardBuilder.build(view).also {
            attachChild(it)
            view.addView(it.view)
        }
    }

    fun detachDashboardView() {
        dashboardRouter?.let {
            detachChild(it)
            view.removeView(it.view)
            dashboardRouter = null
        }
    }
}
