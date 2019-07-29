package me.sdi.github.ribs.authorized

import com.uber.rib.core.ViewRouter
import me.sdi.github.ribs.authorized.main.MainBuilder
import me.sdi.github.ribs.authorized.main.MainRouter
import me.sdi.github.ribs.authorized.repository.CurrentUserRepositoriesBuilder
import me.sdi.github.ribs.authorized.repository.CurrentUserRepositoriesRouter
import me.sdi.github.ribs.authorized.settings.SettingsBuilder
import me.sdi.github.ribs.authorized.settings.SettingsRouter

/**
 * Adds and removes children of {@link DashboardBuilder.DashboardScope}.
 *
 * TODO describe the possible child configurations of this scope.
 */
class DashboardRouter(
    view: DashboardView,
    interactor: DashboardInteractor,
    component: DashboardBuilder.Component,
    private val mainScreenBuilder: MainBuilder,
    private val currentUserRepositoriesScreenBuilder: CurrentUserRepositoriesBuilder,
    private val settingsScreenBuilder: SettingsBuilder
) : ViewRouter<DashboardView, DashboardInteractor, DashboardBuilder.Component>(view, interactor, component) {

    private var mainRouter: MainRouter? = null
    private var currentUserRepositoriesRouter: CurrentUserRepositoriesRouter? = null
    private var settingsRouter: SettingsRouter? = null

    fun showMainScreen() {
        currentUserRepositoriesRouter?.let {
            detachChild(it)
            view.removeView(it.view)
        }
        settingsRouter?.let {
            detachChild(it)
            view.removeView(it.view)
        }

        mainRouter = mainScreenBuilder.build(view)
        mainRouter?.let { view.showScreen(it.view) }
        attachChild(mainRouter)
    }

    fun showCurrentUserProjects() {
        mainRouter?.let {
            detachChild(it)
            view.removeView(it.view)
        }
        settingsRouter?.let {
            detachChild(it)
            view.removeView(it.view)
        }

        currentUserRepositoriesRouter = currentUserRepositoriesScreenBuilder.build(view)
        currentUserRepositoriesRouter?.let { view.showScreen(it.view) }
        attachChild(currentUserRepositoriesRouter)
    }

    fun showSettingsScreen() {
        mainRouter?.let {
            detachChild(it)
            view.removeView(it.view)
        }
        currentUserRepositoriesRouter?.let {
            detachChild(it)
            view.removeView(it.view)
        }

        settingsRouter = settingsScreenBuilder.build(view)
        settingsRouter?.let { view.showScreen(it.view) }
        attachChild(settingsRouter)
    }
}
