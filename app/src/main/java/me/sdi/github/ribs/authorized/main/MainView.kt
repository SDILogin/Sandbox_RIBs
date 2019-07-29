package me.sdi.github.ribs.authorized.main

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.main_view.view.*

/**
 * Top level view for {@link MainBuilder.MainScope}.
 */
class MainView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    ConstraintLayout(context, attrs, defStyle), MainInteractor.MainPresenter {
    override fun showProgressBar() {
        main_view_progress_bar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        main_view_progress_bar.visibility = View.GONE
    }

    override fun showUserId(id: Long) {

    }

    override fun showLogin(login: String) {
        main_view_user_login.text = login
    }

    override fun showAvatar(avatarUrl: String) {
        main_view_avatar.visibility = VISIBLE
        Glide.with(context)
            .load(avatarUrl)
            .into(main_view_avatar)
    }

    override fun hideAvatar() {
        main_view_avatar.visibility = GONE
    }

    override fun showGithubPageUrl(githubPageUrl: String) {
        main_view_github_url.text = githubPageUrl
    }

    override fun showUserName(userName: String) {
        main_view_user_name.text = userName
    }

    override fun showCompany(company: String) {
        main_view_company.visibility = VISIBLE
        main_view_company.text = company
    }

    override fun hideCompany() {
        main_view_company.visibility = GONE
    }

    override fun showLocation(location: String) {
        main_view_location.visibility = VISIBLE
        main_view_location.text = "[$location]"
    }

    override fun hideLocation() {
        main_view_location.visibility = GONE
    }

    override fun showNumberOfPublicRepos(numberOfPublicRepos: Int) {
        main_view_number_of_public_repos.text = "Public repos: $numberOfPublicRepos"
    }

    override fun showNumberOfPublicGists(numberOfPublicGists: Int) {
        main_view_number_of_public_gists.text = "Public gists: $numberOfPublicGists"
    }

    override fun showNumberOfFollowers(numberOfFollowers: Int) {
        main_view_followers.text = "Followers: $numberOfFollowers"
    }

    override fun showNumberOfFollowingUsers(numberOfFollowingUsers: Int) {
        main_view_following.text = "Following: $numberOfFollowingUsers"
    }
}
