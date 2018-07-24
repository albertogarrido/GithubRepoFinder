package net.albertogarrido.githubreposearch.ui.repodetails

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import net.albertogarrido.githubreposearch.network.GithubNetworkRepository
import net.albertogarrido.githubreposearch.network.Subscriber
import net.albertogarrido.githubreposearch.util.Resource


class SubscribersViewModel(application: Application) : AndroidViewModel(application) {
    fun getSubscribersLiveData(owner: String, repoName: String): LiveData<Resource<List<Subscriber>>> =
            GithubNetworkRepository.getSubscribersList(owner, repoName)
}
