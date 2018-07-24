package net.albertogarrido.githubreposearch.ui.repodetails

import android.arch.lifecycle.LiveData
import net.albertogarrido.githubreposearch.network.Subscriber
import net.albertogarrido.githubreposearch.ui.common.GithubRepoFinderView
import net.albertogarrido.githubreposearch.util.ErrorType
import net.albertogarrido.githubreposearch.util.Resource
import net.albertogarrido.githubreposearch.util.Status

class SubscribersPresenter(private val view: SubscribersView, private val viewModel: SubscribersViewModel) {

    interface SubscribersView : GithubRepoFinderView {
        fun populateResults(subscribersResult: List<Subscriber>)
    }

    fun getSubscribersLiveData(owner: String, repoName: String): LiveData<Resource<List<Subscriber>>> =
            viewModel.getSubscribersLiveData(owner, repoName)

    fun handleResponse(resource: Resource<List<Subscriber>>) {
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