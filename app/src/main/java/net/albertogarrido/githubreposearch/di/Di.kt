package net.albertogarrido.githubreposearch.di

import android.content.Context
import com.google.gson.GsonBuilder
import net.albertogarrido.githubreposearch.network.NetworkStatusLiveData
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val GITHUB_URL = "https://api.github.com/"

fun provideRetrofitClient(): Retrofit =
        Retrofit.Builder()
                .baseUrl(GITHUB_URL)
                .client(provideOkHttpClient())
                .addConverterFactory(provideGsonConverter())
                .build()

fun provideGsonConverter(): GsonConverterFactory =
        GsonConverterFactory.create(GsonBuilder().setPrettyPrinting().create())

fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

fun getNetworkStatusLiveData(context: Context): NetworkStatusLiveData = NetworkStatusLiveData(context)
