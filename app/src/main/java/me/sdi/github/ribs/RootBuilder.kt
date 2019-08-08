package me.sdi.github.ribs

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.uber.rib.core.InteractorBaseComponent
import com.uber.rib.core.ViewBuilder
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import me.sdi.github.RuntimeRxSchedulers
import me.sdi.github.RxSchedulers
import me.sdi.github.ribs.authorized.DashboardBuilder
import me.sdi.github.ribs.authorized.settings.SettingsInteractor
import me.sdi.github.ribs.unauthorized.LoginBuilder
import me.sdi.github.ribs.unauthorized.LoginInteractor
import okhttp3.OkHttpClient
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy.CLASS
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Scope

/**
 * Builder for the {@link RootScope}.
 *
 * TODO describe this scope's responsibility as a whole.
 */
class RootBuilder(dependency: ParentComponent) :
    ViewBuilder<RootView, RootRouter, RootBuilder.ParentComponent>(dependency) {

    /**
     * Builds a new [RootRouter].
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new [RootRouter].
     */
    fun build(parentViewGroup: ViewGroup): RootRouter {
        val view = createView(parentViewGroup)
        val interactor = RootInteractor()
        val component = DaggerRootBuilder_Component.builder()
            .parentComponent(dependency)
            .applicationModule(ApplicationModule(parentViewGroup.context.applicationContext))
            .view(view)
            .interactor(interactor)
            .loginListener(interactor)
            .logoutListener(interactor)
            .build()
        return component.rootRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): RootView? {
        val rootView = RootView(parentViewGroup.context)
        rootView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        return rootView
    }

    interface ParentComponent {
        // TODO: Define dependencies required from your parent interactor here.
    }

    @dagger.Module
    class ApplicationModule(
        private val applicationContext: Context
    ) {
        @Provides
        @RootScope
        fun applicationContext() = applicationContext
    }

    @dagger.Module
    abstract class Module {

        @RootScope
        @Binds
        internal abstract fun presenter(view: RootView): RootInteractor.RootPresenter

        @dagger.Module
        companion object {

            @RootScope
            @Provides
            @JvmStatic
            internal fun router(
                component: Component,
                view: RootView,
                interactor: RootInteractor
            ): RootRouter {
                return RootRouter(view, interactor, component, LoginBuilder(component), DashboardBuilder(component))
            }

            @RootScope
            @Provides
            @JvmStatic
            internal fun moshi() = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

            @RootScope
            @Provides
            @JvmStatic
            internal fun okHttpClient() = OkHttpClient.Builder()
                .callTimeout(2, TimeUnit.SECONDS)
                .writeTimeout(2, TimeUnit.SECONDS)
                .readTimeout(2, TimeUnit.SECONDS)
                .connectTimeout(2, TimeUnit.SECONDS)
                .build()

            @RootScope
            @Provides
            @JvmStatic
            internal fun tokenStorage(context: Context): TokenStorage = SharePreferencesTokenStorage(context)

            @RootScope
            @Provides
            @JvmStatic
            internal fun rxSchedulers(): RxSchedulers = RuntimeRxSchedulers()
        }

        // TODO: Create provider methods for dependencies created by this Rib. These should be static.
    }

    @RootScope
    @dagger.Component(
        modules = arrayOf(Module::class, ApplicationModule::class),
        dependencies = arrayOf(ParentComponent::class)
    )
    interface Component : InteractorBaseComponent<RootInteractor>, BuilderComponent,
        LoginBuilder.ParentComponent,
        DashboardBuilder.ParentComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: RootInteractor): Builder

            @BindsInstance
            fun loginListener(loginInteractorListener: LoginInteractor.Listener): Builder

            @BindsInstance
            fun logoutListener(logoutListener: SettingsInteractor.Listener): Builder

            @BindsInstance
            fun view(view: RootView): Builder

            fun applicationModule(applicationModule: ApplicationModule): Builder
            fun parentComponent(component: ParentComponent): Builder

            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun rootRouter(): RootRouter
    }

    @Scope
    @Retention(CLASS)
    internal annotation class RootScope

    @Qualifier
    @Retention(CLASS)
    internal annotation class RootInternal
}
