package me.sdi.github.ribs.authorized

import android.content.Context
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
import me.sdi.github.core.User
import me.sdi.github.network.GithubUserInfoGateway
import me.sdi.github.network.UserInfoGateway
import me.sdi.github.ribs.TokenStorage
import me.sdi.github.ribs.authorized.main.MainBuilder
import me.sdi.github.ribs.authorized.repository.CurrentUserRepositoriesBuilder
import me.sdi.github.ribs.authorized.settings.SettingsBuilder
import okhttp3.OkHttpClient
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy.CLASS
import javax.inject.Qualifier
import javax.inject.Scope

/**
 * Builder for the {@link DashboardScope}.
 *
 * TODO describe this scope's responsibility as a whole.
 */
class DashboardBuilder(dependency: ParentComponent) :
    ViewBuilder<DashboardView, DashboardRouter, DashboardBuilder.ParentComponent>(dependency) {

    /**
     * Builds a new [DashboardRouter].
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new [DashboardRouter].
     */
    fun build(parentViewGroup: ViewGroup): DashboardRouter {
        val view = createView(parentViewGroup)
        val interactor = DashboardInteractor()
        val component = DaggerDashboardBuilder_Component.builder()
            .parentComponent(dependency)
            .view(view)
            .interactor(interactor)
            .build()
        return component.dashboardRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): DashboardView? {
        return inflater.inflate(R.layout.dashboard_view, parentViewGroup, false) as DashboardView
    }

    interface ParentComponent {
        fun applicationContext(): Context
        fun tokenStorage(): TokenStorage
        fun okHttpClient(): OkHttpClient
        fun moshi(): Moshi
        fun rxSchedulers(): RxSchedulers
    }

    @dagger.Module
    abstract class Module {

        @DashboardScope
        @Binds
        internal abstract fun presenter(view: DashboardView): DashboardInteractor.DashboardPresenter

        @dagger.Module
        companion object {

            @DashboardScope
            @Provides
            @JvmStatic
            internal fun userStorage(context: Context, moshi: Moshi): UserStorage =
                SharedPreferencesUserStorage(context, moshi.adapter(User::class.java))

            @DashboardScope
            @Provides
            @JvmStatic
            internal fun router(
                component: Component,
                view: DashboardView,
                interactor: DashboardInteractor
            ): DashboardRouter {
                return DashboardRouter(
                    view,
                    interactor,
                    component,
                    MainBuilder(component),
                    CurrentUserRepositoriesBuilder(component),
                    SettingsBuilder(component)
                )
            }

            @DashboardScope
            @Provides
            @JvmStatic
            internal fun userInfoGateway(
                okHttpClient: OkHttpClient,
                tokenStorage: TokenStorage,
                moshi: Moshi
            ): UserInfoGateway = GithubUserInfoGateway(okHttpClient, tokenStorage, moshi)
        }

        // TODO: Create provider methods for dependencies created by this Rib. These should be static.
    }

    @DashboardScope
    @dagger.Component(modules = arrayOf(Module::class), dependencies = arrayOf(ParentComponent::class))
    interface Component :
        MainBuilder.ParentComponent,
        SettingsBuilder.ParentComponent,
        CurrentUserRepositoriesBuilder.ParentComponent,
        InteractorBaseComponent<DashboardInteractor>, BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: DashboardInteractor): Builder

            @BindsInstance
            fun view(view: DashboardView): Builder

            fun parentComponent(component: ParentComponent): Builder
            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun dashboardRouter(): DashboardRouter
    }

    @Scope
    @Retention(CLASS)
    internal annotation class DashboardScope

    @Qualifier
    @Retention(CLASS)
    internal annotation class DashboardInternal
}
