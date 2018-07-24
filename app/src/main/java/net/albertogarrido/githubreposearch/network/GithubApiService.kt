package net.albertogarrido.githubreposearch.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApiService {

    companion object {
        const val E_SEARCH_REPOSITORIES = "search/repositories"
        const val E_SUBSCRIBERS = "repos/{owner}/{repoName}/subscribers"
    }

    @GET(E_SEARCH_REPOSITORIES)
    fun repositories(@Query("q") term: String): Call<RepositoriesResponse>

    @GET(E_SEARCH_REPOSITORIES)
    fun repositories(@Query("q") term: String,
                     @Query("sort") sort: String = "stars",
                     @Query("order") order: String = "asc"): Call<RepositoriesResponse>

    @GET(E_SUBSCRIBERS)
    fun subscribers(@Path("owner") owner: String,
                    @Path("repoName") repoName: String): Call<List<Subscriber>>


}