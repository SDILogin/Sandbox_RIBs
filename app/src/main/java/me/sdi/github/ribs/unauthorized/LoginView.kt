package me.sdi.github.ribs.unauthorized

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import io.reactivex.Observable
import kotlinx.android.synthetic.main.login_view.view.*
import me.sdi.github.clicks

/**
 * Top level view for {@link LoginBuilder.LoginScope}.
 */
class LoginView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    ConstraintLayout(context, attrs, defStyle), LoginInteractor.LoginPresenter {

    override fun showLoginFailedMessage() {
        passwordlInputLayout.error = "Failed to login"
    }

    override fun loginClicks(): Observable<Pair<String, String>> {
        return loginButton.clicks()
            .map { Pair(emailEditText.text.toString(), passwordlEditText.text.toString()) }
    }
}
