package me.sdi.github.ribs.authorized

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import io.reactivex.Observable
import kotlinx.android.synthetic.main.dashboard_view.view.*
import me.sdi.github.R
import me.sdi.github.selectedMenuItem

/**
 * Top level view for {@link DashboardBuilder.DashboardScope}.
 */
class DashboardView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    ConstraintLayout(context, attrs, defStyle), DashboardInteractor.DashboardPresenter {

    fun showScreen(screen: View) {
        val constraintSet = ConstraintSet().apply {
            connect(screen.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(screen.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            connect(screen.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            connect(screen.id, ConstraintSet.BOTTOM, dashboard_bottom_navigation_view.id, ConstraintSet.TOP)
        }
        addView(screen)
        constraintSet.applyTo(this)
    }

    override fun navigationItemSelected(): Observable<DashboardInteractor.DashboardPresenter.Screen> {
        return dashboard_bottom_navigation_view.selectedMenuItem()
            .map { menuItem ->
                when (menuItem.itemId) {
                    R.id.navigation_main -> DashboardInteractor.DashboardPresenter.Screen.MAIN
                    R.id.navigation_my_repositories -> DashboardInteractor.DashboardPresenter.Screen.CURRENT_USER_REPOSITORIES
                    R.id.navigation_settings -> DashboardInteractor.DashboardPresenter.Screen.SETTINGS
                    else -> throw IllegalArgumentException()
                }
            }
    }
}
