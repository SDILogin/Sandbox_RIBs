package me.sdi.github.ribs.authorized.repository

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.current_user_repositories.view.*
import me.sdi.github.core.Repository

/**
 * Top level view for {@link CurrentUserRepositoriesBuilder.CurrentUserRepositoriesScope}.
 */
class CurrentUserRepositoriesView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle), CurrentUserRepositoriesInteractor.CurrentUserRepositoriesPresenter {

    val adapter: CurrentUserRepositoriesAdapter = CurrentUserRepositoriesAdapter()

    override fun showProgressBar() {
        current_user_repositories_progress_bar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        current_user_repositories_progress_bar.visibility = View.GONE
    }

    override fun showRepositories(repositories: List<Repository>) {
        adapter.update(repositories)
    }

    override fun showEmptyScreenPlaceholder() {
        current_user_repositories_empty_screen_indicator.visibility = View.VISIBLE
    }

    override fun hideEmptyScreenPlaceholder() {
        current_user_repositories_empty_screen_indicator.visibility = View.GONE
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        current_user_repositories_list.layoutManager = LinearLayoutManager(context)
        current_user_repositories_list.adapter = adapter
    }
}
