package me.sdi.github.ribs.authorized.settings

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import io.reactivex.Observable
import kotlinx.android.synthetic.main.settings_view.view.*
import me.sdi.github.clicks

/**
 * Top level view for {@link SettingsBuilder.SettingsScope}.
 */
class SettingsView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    FrameLayout(context, attrs, defStyle), SettingsInteractor.SettingsPresenter {

    override fun logoutRequests(): Observable<Unit> = settings_screen_logout.clicks()
}
