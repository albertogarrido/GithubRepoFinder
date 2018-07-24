package net.albertogarrido.githubreposearch.ui.repofinder

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import net.albertogarrido.githubreposearch.network.GithubNetworkRepository
import net.albertogarrido.githubreposearch.network.GithubRepo
import net.albertogarrido.githubreposearch.util.Resource

class GithubReposViewModel(application: Application) : AndroidViewModel(application) {

    private var reposListLiveData: LiveData<Resource<List<GithubRepo>>> =
            MutableLiveData<Resource<List<GithubRepo>>>()

    fun getReposLiveData(): LiveData<Resource<List<GithubRepo>>> = reposListLiveData

    fun refreshLiveData(term: String): LiveData<Resource<List<GithubRepo>>> =
            GithubNetworkRepository.getGithubReposList(term)
}
