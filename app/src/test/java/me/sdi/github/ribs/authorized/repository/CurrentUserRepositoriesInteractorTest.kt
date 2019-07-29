package me.sdi.github.ribs.authorized.repository

import com.uber.rib.core.InteractorHelper
import com.uber.rib.core.RibTestBasePlaceholder
import me.sdi.github.core.Repository
import me.sdi.github.network.UserInfoGateway
import me.sdi.github.ribs.fakes.FakeUserInfoGateway
import me.sdi.github.ribs.fakes.TestRxSchedulers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class CurrentUserRepositoriesInteractorTest : RibTestBasePlaceholder() {

    @Mock
    internal lateinit var router: CurrentUserRepositoriesRouter

    private lateinit var presenter: FakeCurrentUserRepositoriesPresenter
    private lateinit var interactor: CurrentUserRepositoriesInteractor

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        presenter = FakeCurrentUserRepositoriesPresenter()
        interactor = createInteractorWithUserInfoGateway(FakeUserInfoGateway())
    }

    /**
     * display loaded repositories
     */
    @Test
    fun when_loaded_some_repositories_show_them() {
        // do
        InteractorHelper.attach(interactor, presenter, router, null)
        InteractorHelper.detach(interactor)

        // verify
        assertThat(presenter.getDisplayedRepositories().isNotEmpty(), `is`(true))
    }

    /**
     * display empty screen placeholder
     */
    @Test
    fun when_user_haven_repositories_show_empty_screen_placeholder() {
        // preconditions
        interactor = createInteractorWithUserInfoGateway(EmptyRepositoriesFakeUserInfoGateway())

        // do
        InteractorHelper.attach(interactor, presenter, router, null)
        InteractorHelper.detach(interactor)

        // verify
        assertThat(presenter.getEmptyScreenVisibility(), `is`(true))
    }

    fun createInteractorWithUserInfoGateway(userInfoGateway: UserInfoGateway) =
        TestCurrentUserRepositoriesInteractor.create(presenter, userInfoGateway, TestRxSchedulers())

    class EmptyRepositoriesFakeUserInfoGateway : UserInfoGateway by FakeUserInfoGateway() {

        override fun repositories(): List<Repository> = emptyList()
    }

    class FakeCurrentUserRepositoriesPresenter : CurrentUserRepositoriesInteractor.CurrentUserRepositoriesPresenter {

        private var progressBarVisibility = false
        private var emptyListPlaceholderVisibility = false
        private var displayedRepositories: List<Repository> = emptyList()

        fun getProgressBarVisibility() = progressBarVisibility
        fun getEmptyScreenVisibility() = emptyListPlaceholderVisibility
        fun getDisplayedRepositories() = displayedRepositories

        override fun showProgressBar() {
            progressBarVisibility = true
        }

        override fun hideProgressBar() {
            progressBarVisibility = false
        }

        override fun showRepositories(repositories: List<Repository>) {
            displayedRepositories = repositories
        }

        override fun showEmptyScreenPlaceholder() {
            emptyListPlaceholderVisibility = true
        }

        override fun hideEmptyScreenPlaceholder() {
            emptyListPlaceholderVisibility = false
        }

    }
}