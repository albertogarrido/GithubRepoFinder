package net.albertogarrido.githubreposearch.ui.repodetails

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.MenuItem
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_repo_details.*
import kotlinx.android.synthetic.main.content_details_activity.*
import net.albertogarrido.githubreposearch.R
import net.albertogarrido.githubreposearch.di.getNetworkStatusLiveData
import net.albertogarrido.githubreposearch.network.GithubRepo
import net.albertogarrido.githubreposearch.network.NetworkStatusLiveData
import net.albertogarrido.githubreposearch.network.Subscriber
import net.albertogarrido.githubreposearch.util.ErrorType
import net.albertogarrido.githubreposearch.util.Resource
import net.albertogarrido.githubreposearch.util.gone
import net.albertogarrido.githubreposearch.util.visible
import org.parceler.Parcels

class RepoDetailsActivity : AppCompatActivity(), SubscribersPresenter.SubscribersView {

    private val presenter: SubscribersPresenter by lazy {
        SubscribersPresenter(this, createViewModel())
    }

    private val networkStatusLiveData: NetworkStatusLiveData by lazy {
        getNetworkStatusLiveData(this)
    }

    private lateinit var subscribersListLiveData: LiveData<Resource<List<Subscriber>>>
    private var watchersCount: Int = 0

    companion object {
        private const val REPO_EXTRAS = "repo_extras"
        private const val SHARED_ELEMENT_IMAGE = "shared_element_image"
        fun createIntent(context: Context, repo: GithubRepo, sharedImageName: String): Intent =
                Intent(context, RepoDetailsActivity::class.java)
                        .putExtra(REPO_EXTRAS, Parcels.wrap(repo))
                        .putExtra(SHARED_ELEMENT_IMAGE, sharedImageName)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo_details)
        configureToolbar()
    }

    override fun onResume() {
        super.onResume()
        populateIncomingData()
    }

    private fun startRetrievingData(owner: String, repoName: String) {
        observeConnectivityChanges()
        if (NetworkStatusLiveData.isConnected(this)) {
            observeLiveData(owner, repoName)
        } else {
            showError(ErrorType.NETWORK, null)
        }
    }

    private fun createViewModel() =
            ViewModelProviders.of(this).get(SubscribersViewModel::class.java)

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
            when (item.itemId) {
                android.R.id.home -> {
                    supportFinishAfterTransition()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }


    private fun populateIncomingData() {
        supportPostponeEnterTransition()
        val extras = intent.extras
        val repo = Parcels.unwrap<GithubRepo>(extras.getParcelable(REPO_EXTRAS))
        toolbar.title = repo.fullName
        repoName.text = repo.name
        repoDescription.text = repo.description ?: ""
        countForks.text = repo.forksCount.toString()
        countWatchers.text = repo.watchersCount.toString()
        watchersCount = repo.watchersCount

        val imageTransitionName = extras.getString(SHARED_ELEMENT_IMAGE)
        authorAvatar.transitionName = imageTransitionName

        Picasso.get()
                .load(repo.owner.avatarUrl)
                .resizeDimen(R.dimen.image_side_size, R.dimen.image_side_size)
                .centerCrop()
                .into(authorAvatar)

        startRetrievingData(repo.owner.login, repo.name)
    }

    private fun configureToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.title = getString(R.string.search_github)
    }

    private fun observeConnectivityChanges() {
        networkStatusLiveData.observe(this, Observer { isConnected ->
            if (isConnected == null || !isConnected) {
                showError(ErrorType.NETWORK, null)
            }
        })
    }

    private fun observeLiveData(owner: String, repoName: String) {
        toggleLoading(true)
        subscribersListLiveData = presenter.getSubscribersLiveData(owner, repoName)
        subscribersListLiveData.observe(this, Observer { resource ->
            presenter.handleResponse(resource!!)
        })
    }

    override fun populateResults(subscribersResults: List<Subscriber>) {
        toggleLoading(false)
        configureRecyclerView()
        subscribers.text = resources.getString(R.string.subscribers_count, watchersCount)
        val adapter = SubscribersAdapter(subscribersResults)
        subscribersList.adapter = adapter
    }

    private fun configureRecyclerView() {
        subscribersList.setHasFixedSize(true)
        val layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        subscribersList.layoutManager = layoutManager
    }

    override fun showError(errorType: ErrorType, optionalMessage: String?) {
        val message = when (errorType) {
            ErrorType.NETWORK -> "Not connection detected ${optionalMessage ?: ""}"
            ErrorType.SERVER -> "Oops there was a problem with github server ${optionalMessage
                    ?: ""}"
            ErrorType.NOT_FOUND -> "There are not matching repositories ${optionalMessage ?: ""}"
            ErrorType.OTHER -> "Something really weird happened ${optionalMessage ?: ""}"
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun toggleLoading(show: Boolean) {
        if (show) {
            progressBarContainer.visible()
        } else {
            progressBarContainer.gone()
        }
    }

}
