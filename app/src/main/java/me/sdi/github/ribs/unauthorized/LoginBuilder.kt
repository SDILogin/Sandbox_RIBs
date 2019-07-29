package me.sdi.github.ribs.unauthorized

import android.view.LayoutInflater
import android.view.ViewGroup
import com.squareup.moshi.Moshi
import com.uber.rib.core.InteractorBaseComponent
import com.uber.rib.core.ViewBuilder
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import me.sdi.github.R
import me.sdi.github.RxSchedulers
import me.sdi.github.network.GithubLoginGateway
import me.sdi.github.network.LoginGateway
import me.sdi.github.ribs.TokenStorage
import okhttp3.OkHttpClient
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy.CLASS
import javax.inject.Qualifier
import javax.inject.Scope

/**
 * Builder for the {@link LoginScope}.
 *
 * TODO describe this scope's responsibility as a whole.
 */
class LoginBuilder(dependency: ParentComponent) :
    ViewBuilder<LoginView, LoginRouter, LoginBuilder.ParentComponent>(dependency) {

    /**
     * Builds a new [LoginRouter].
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new [LoginRouter].
     */
    fun build(parentViewGroup: ViewGroup?): LoginRouter {
        val view = createView(parentViewGroup)
        val interactor = LoginInteractor()
        val component = DaggerLoginBuilder_Component.builder()
            .parentComponent(dependency)
            .view(view)
            .interactor(interactor)
            .build()
        return component.loginRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): LoginView? {
        return inflater.inflate(R.layout.login_view, parentViewGroup, false) as LoginView
    }

    interface ParentComponent {
        fun loginListener(): LoginInteractor.Listener
        fun tokenStorage(): TokenStorage
        fun okHttpClient(): OkHttpClient
        fun moshi(): Moshi
        fun rxSchedulers(): RxSchedulers
    }

    @dagger.Module
    abstract class Module {

        @LoginScope
        @Binds
        internal abstract fun presenter(view: LoginView): LoginInteractor.LoginPresenter

        @dagger.Module
        companion object {

            @LoginScope
            @Provides
            @JvmStatic
            internal fun router(
                component: Component,
                view: LoginView,
                interactor: LoginInteractor
            ): LoginRouter {
                return LoginRouter(view, interactor, component)
            }

            @LoginScope
            @Provides
            @JvmStatic
            internal fun loginGateway(okHttpClient: OkHttpClient, moshi: Moshi): LoginGateway =
                GithubLoginGateway(okHttpClient, moshi)
        }

        // TODO: Create provider methods for dependencies created by this Rib. These should be static.
    }

    @LoginScope
    @dagger.Component(modules = arrayOf(Module::class), dependencies = arrayOf(ParentComponent::class))
    interface Component : InteractorBaseComponent<LoginInteractor>, BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: LoginInteractor): Builder

            @BindsInstance
            fun view(view: LoginView): Builder

            fun parentComponent(component: ParentComponent): Builder
            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun loginRouter(): LoginRouter
    }

    @Scope
    @Retention(CLASS)
    internal annotation class LoginScope

    @Qualifier
    @Retention(CLASS)
    internal annotation class LoginInternal
}
