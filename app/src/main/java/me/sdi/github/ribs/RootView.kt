package me.sdi.github.ribs

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import android.widget.ProgressBar

/**
 * Top level view for {@link RootBuilder.RootScope}.
 */
open class RootView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    FrameLayout(context, attrs, defStyle), RootInteractor.RootPresenter {

    private val progressBar: ProgressBar = ProgressBar(context).apply {
        layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT, Gravity.CENTER)
    }

    override fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    init {
        addView(progressBar)
    }
}
