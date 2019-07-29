package me.sdi.github.ribs.authorized.repository

import com.uber.rib.core.ViewRouter

/**
 * Adds and removes children of {@link CurrentUserRepositoriesBuilder.CurrentUserRepositoriesScope}.
 *
 * TODO describe the possible child configurations of this scope.
 */
class CurrentUserRepositoriesRouter(
    view: CurrentUserRepositoriesView,
    interactor: CurrentUserRepositoriesInteractor,
    component: CurrentUserRepositoriesBuilder.Component
) : ViewRouter<CurrentUserRepositoriesView, CurrentUserRepositoriesInteractor, CurrentUserRepositoriesBuilder.Component>(
    view,
    interactor,
    component
)
