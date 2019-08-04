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
        }?.also {
            currentUserRepositoriesRouter = null
        }

        settingsRouter?.let {
            detachChild(it)
            view.removeView(it.view)
        }?.also {
            settingsRouter = null
        }

        if (mainRouter == null) {
            mainRouter = mainScreenBuilder.build(view)
            mainRouter?.let { view.showScreen(it.view) }
            attachChild(mainRouter)
        }
    }

    fun showCurrentUserProjects() {
        mainRouter?.let {
            detachChild(it)
            view.removeView(it.view)
        }?.also {
            mainRouter = null
        }
        settingsRouter?.let {
            detachChild(it)
            view.removeView(it.view)
        }?.also {
            settingsRouter = null
        }

        if (currentUserRepositoriesRouter == null) {
            currentUserRepositoriesRouter = currentUserRepositoriesScreenBuilder.build(view)
            currentUserRepositoriesRouter?.let { view.showScreen(it.view) }
            attachChild(currentUserRepositoriesRouter)
        }
    }

    fun showSettingsScreen() {
        mainRouter?.let {
            detachChild(it)
            view.removeView(it.view)
        }.also {
            mainRouter = null
        }
        currentUserRepositoriesRouter?.let {
            detachChild(it)
            view.removeView(it.view)
        }.also {
            currentUserRepositoriesRouter = null
        }

        if (settingsRouter == null) {
            settingsRouter = settingsScreenBuilder.build(view)
            settingsRouter?.let { view.showScreen(it.view) }
            attachChild(settingsRouter)
        }
    }
}
