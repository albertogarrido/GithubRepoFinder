package net.albertogarrido.githubreposearch.ui.repofinder

import android.arch.lifecycle.LiveData
import net.albertogarrido.githubreposearch.network.GithubRepo
import net.albertogarrido.githubreposearch.ui.common.GithubRepoFinderView
import net.albertogarrido.githubreposearch.util.ErrorType
import net.albertogarrido.githubreposearch.util.Resource
import net.albertogarrido.githubreposearch.util.Status

class GithubReposPresenter(private val view: GithubReposView, private val viewModel: GithubReposViewModel) {

    interface GithubReposView : GithubRepoFinderView {
        fun populateResults(repos: List<GithubRepo>)
    }

    fun getGithubReposLiveData(term: String = "", refresh: Boolean = false): LiveData<Resource<List<GithubRepo>>> =
            if (refresh) {
                viewModel.refreshLiveData(term)
            } else {
                viewModel.getReposLiveData()
            }

    fun handleResponse(resource: Resource<List<GithubRepo>>) {
        when (resource.status) {
            Status.SUCCESS -> view.populateResults(resource.data)
            Status.EMPTY_DATA -> {
                view.toggleLoading(false)
                view.showError(ErrorType.NOT_FOUND, null)
            }
            Status.LOADING -> {
                view.toggleLoading(true)
            }
            Status.ERROR -> {
                view.toggleLoading(false)
                view.showError(ErrorType.OTHER, resource.message)
            }
        }
    }
}