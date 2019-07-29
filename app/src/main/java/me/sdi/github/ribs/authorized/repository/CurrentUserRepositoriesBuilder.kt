package me.sdi.github.ribs.authorized.repository

import android.view.LayoutInflater
import android.view.ViewGroup
import com.uber.rib.core.InteractorBaseComponent
import com.uber.rib.core.ViewBuilder
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import kotlinx.android.synthetic.main.current_user_repositories.view.*
import me.sdi.github.R
import me.sdi.github.RxSchedulers
import me.sdi.github.network.UserInfoGateway
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy.CLASS
import javax.inject.Qualifier
import javax.inject.Scope

/**
 * Builder for the {@link CurrentUserRepositoriesScope}.
 *
 * TODO describe this scope's responsibility as a whole.
 */
class CurrentUserRepositoriesBuilder(dependency: ParentComponent) :
    ViewBuilder<CurrentUserRepositoriesView, CurrentUserRepositoriesRouter, CurrentUserRepositoriesBuilder.ParentComponent>(
        dependency
    ) {

    /**
     * Builds a new [CurrentUserRepositoriesRouter].
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new [CurrentUserRepositoriesRouter].
     */
    fun build(parentViewGroup: ViewGroup): CurrentUserRepositoriesRouter {
        val view = createView(parentViewGroup)
        val interactor = CurrentUserRepositoriesInteractor()
        val component = DaggerCurrentUserRepositoriesBuilder_Component.builder()
            .parentComponent(dependency)
            .view(view)
            .interactor(interactor)
            .build()
        return component.currentuserrepositoriesRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): CurrentUserRepositoriesView? {
        return inflater.inflate(
            R.layout.current_user_repositories,
            parentViewGroup,
            false
        ) as CurrentUserRepositoriesView
    }

    interface ParentComponent {
        fun userInfoGateway(): UserInfoGateway
        fun rxSchedulers(): RxSchedulers
    }

    @dagger.Module
    abstract class Module {

        @CurrentUserRepositoriesScope
        @Binds
        internal abstract fun presenter(view: CurrentUserRepositoriesView): CurrentUserRepositoriesInteractor.CurrentUserRepositoriesPresenter

        @dagger.Module
        companion object {

            @CurrentUserRepositoriesScope
            @Provides
            @JvmStatic
            internal fun router(
                component: Component,
                view: CurrentUserRepositoriesView,
                interactor: CurrentUserRepositoriesInteractor
            ): CurrentUserRepositoriesRouter {
                return CurrentUserRepositoriesRouter(view, interactor, component)
            }
        }

        // TODO: Create provider methods for dependencies created by this Rib. These should be static.
    }

    @CurrentUserRepositoriesScope
    @dagger.Component(modules = arrayOf(Module::class), dependencies = arrayOf(ParentComponent::class))
    interface Component : InteractorBaseComponent<CurrentUserRepositoriesInteractor>, BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: CurrentUserRepositoriesInteractor): Builder

            @BindsInstance
            fun view(view: CurrentUserRepositoriesView): Builder

            fun parentComponent(component: ParentComponent): Builder
            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun currentuserrepositoriesRouter(): CurrentUserRepositoriesRouter
    }

    @Scope
    @Retention(CLASS)
    internal annotation class CurrentUserRepositoriesScope

    @Qualifier
    @Retention(CLASS)
    internal annotation class CurrentUserRepositoriesInternal
}
