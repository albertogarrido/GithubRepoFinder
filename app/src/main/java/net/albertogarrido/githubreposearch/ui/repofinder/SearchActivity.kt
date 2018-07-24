package net.albertogarrido.githubreposearch.ui.repofinder

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.content_search_activity.*
import net.albertogarrido.githubreposearch.R
import net.albertogarrido.githubreposearch.di.getNetworkStatusLiveData
import net.albertogarrido.githubreposearch.network.GithubRepo
import net.albertogarrido.githubreposearch.network.NetworkStatusLiveData
import net.albertogarrido.githubreposearch.ui.repodetails.RepoDetailsActivity
import net.albertogarrido.githubreposearch.util.*


class SearchActivity : AppCompatActivity(), GithubReposPresenter.GithubReposView {

    private val presenter: GithubReposPresenter by lazy {
        GithubReposPresenter(this, createViewModel())
    }

    private val networkStatusLiveData: NetworkStatusLiveData by lazy {
        getNetworkStatusLiveData(this)
    }

    private lateinit var githubReposListLiveData: LiveData<Resource<List<GithubRepo>>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        start()
        configureToolbar()
        configureViews()
    }

    private fun configureViews() {
        searchInput.onTextChanged { term, countBefore, count ->
            if (count > 2) {
                presenter.getGithubReposLiveData(term, countBefore != count).observe(this,
                        Observer { resource ->
                            if (NetworkStatusLiveData.isConnected(this)) {
                                presenter.handleResponse(resource!!)
                            } else {
                                Toast.makeText(this, "Not connected to internet", Toast.LENGTH_SHORT).show()
                            }
                        }
                )
            }
        }
    }

    private fun start() {
        observeConnectivityChanges()
        if (!NetworkStatusLiveData.isConnected(this)) {
            showError(ErrorType.NETWORK, null)
        }
    }

    private fun configureToolbar() {
        setSupportActionBar(toolbar)
        toolbar.title = getString(R.string.search_github)
    }

    private fun observeConnectivityChanges() {
        networkStatusLiveData.observe(this, Observer { isConnected ->
            if (isConnected == null || !isConnected) {
                showError(ErrorType.NETWORK, null)
            }
        })
    }

    private fun createViewModel() =
            ViewModelProviders.of(this).get(GithubReposViewModel::class.java)

    override fun populateResults(repos: List<GithubRepo>) {
        toggleLoading(false)
        configureRecyclerView()
        val adapter = GithubRepoAdapter(repos)
        adapter.addRecyclerItemClickListener { _, imageShared, position ->
            val repo = repos[position]
            val sharedImageName = ViewCompat.getTransitionName(imageShared)
            val githubRepoIntent = RepoDetailsActivity.createIntent(this, repo, sharedImageName)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, imageShared, sharedImageName)
            startActivity(githubRepoIntent, options.toBundle())
        }
        reposList.adapter = adapter
    }

    private fun configureRecyclerView() {
        reposList.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        reposList.layoutManager = layoutManager
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
