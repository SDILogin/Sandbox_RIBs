package me.sdi.github.ribs.unauthorized

import com.uber.rib.core.ViewRouter

/**
 * Adds and removes children of {@link LoginBuilder.LoginScope}.
 *
 * TODO describe the possible child configurations of this scope.
 */
class LoginRouter(
    view: LoginView,
    interactor: LoginInteractor,
    component: LoginBuilder.Component
) : ViewRouter<LoginView, LoginInteractor, LoginBuilder.Component>(view, interactor, component)
