package net.albertogarrido.githubreposearch.network

import android.arch.lifecycle.MutableLiveData
import net.albertogarrido.githubreposearch.di.provideRetrofitClient
import net.albertogarrido.githubreposearch.util.*
import retrofit2.Retrofit

object GithubNetworkRepository {

    private val retrofit: Retrofit = provideRetrofitClient()

    private val githubApiService: GithubApiService by lazy {
        retrofit.create(GithubApiService::class.java)
    }

    fun getGithubReposList(term: String):
            MutableLiveData<Resource<List<GithubRepo>>> {

        val liveData = MutableLiveData<Resource<List<GithubRepo>>>()

        liveData.postValue(resourceLoading(listOf()))

        githubApiService.repositories(term).enqueue(
                { response ->
                    when {
                        response.body() == null -> liveData.setValue(resourceEmpty(response.message(), listOf()))
                        response.isSuccessful -> {
                            // this cannot be null, but smart cast complaints, hence the double !!
                            val repositoriesResponse: RepositoriesResponse = response.body()!!
                            if (repositoriesResponse.githubRepos.isEmpty()) {
                                liveData.setValue(resourceEmpty(response.message(), repositoriesResponse.githubRepos))
                            } else {
                                liveData.setValue(resourceSuccess(repositoriesResponse.githubRepos))
                            }
                        }
                        else -> liveData.setValue(resourceError(response.message(), listOf()))
                    }
                },
                { throwable ->
                    liveData.setValue(resourceError(throwable.message
                            ?: "Unknown error", listOf()))
                }
        )
        return liveData
    }

    fun getSubscribersList(owner: String, repoName: String):
            MutableLiveData<Resource<List<Subscriber>>> {

        val liveData = MutableLiveData<Resource<List<Subscriber>>>()

        liveData.postValue(resourceLoading(listOf()))

        githubApiService.subscribers(owner, repoName).enqueue(
                { response ->
                    when {
                        response.body() == null -> liveData.setValue(resourceEmpty(response.message(), listOf()))
                        response.isSuccessful -> {
                            // this cannot be null, but smart cast complaints, hence the double !!
                            val subscribers: List<Subscriber> = response.body()!!
                            if (subscribers.isEmpty()) {
                                liveData.setValue(resourceEmpty(response.message(), subscribers))
                            } else {
                                liveData.setValue(resourceSuccess(subscribers))
                            }
                        }
                        else -> liveData.setValue(resourceError(response.message(), listOf()))
                    }
                },
                { throwable ->
                    liveData.setValue(resourceError(throwable.message
                            ?: "Unknown error", listOf()))
                }
        )
        return liveData
    }
}