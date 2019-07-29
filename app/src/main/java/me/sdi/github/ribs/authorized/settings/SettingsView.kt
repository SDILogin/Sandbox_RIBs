package me.sdi.github.ribs.authorized.settings

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * Top level view for {@link SettingsBuilder.SettingsScope}.
 */
class SettingsView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    FrameLayout(context, attrs, defStyle), SettingsInteractor.SettingsPresenter
