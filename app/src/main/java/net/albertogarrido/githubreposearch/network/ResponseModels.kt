package net.albertogarrido.githubreposearch.network

import com.google.gson.annotations.SerializedName
import org.parceler.Parcel
import org.parceler.ParcelConstructor

data class RepositoriesResponse(
        @SerializedName("total_count") val totalCount: Int,
        @SerializedName("items") val githubRepos: List<GithubRepo>
)

@Parcel(Parcel.Serialization.BEAN)
data class GithubRepo @ParcelConstructor constructor(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("full_name") val fullName: String,
        @SerializedName("description") val description: String?,
        @SerializedName("forks_count") val forksCount: Int,
        @SerializedName("subscribers_url") val subscribersUrl: String,
        @SerializedName("watchers_count") val watchersCount: Int,
        @SerializedName("owner") val owner: Owner
)

@Parcel(Parcel.Serialization.BEAN)
data class Owner @ParcelConstructor constructor(
        @SerializedName("id") val id: Int,
        @SerializedName("login") val login: String,
        @SerializedName("avatar_url") val avatarUrl: String
)

@Parcel(Parcel.Serialization.BEAN)
data class Subscriber @ParcelConstructor constructor(
        @SerializedName("id") val id: Int,
        @SerializedName("login") val login: String,
        @SerializedName("avatar_url") val avatarUrl: String
)


