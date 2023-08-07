package com.st10083770.myapplicationnewsapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsViewModel : ViewModel() {
    // LiveData to hold the list of news articles
    private val _newsArticles: MutableLiveData<List<NewsArticle>> = MutableLiveData()
    val newsArticles: LiveData<List<NewsArticle>>
        get() = _newsArticles

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://newsapi.org/") // Replace with the correct News API base URL
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val newsApiService: NewsApiService = retrofit.create(NewsApiService::class.java)

    fun fetchNewsArticles() {
        val country = "us" // Replace with the desired country code
        val apiKey = "ab3dee3d075a4e4c82ef0f2ff4163bcd" // Replace with your actual News API key

        val call: Call<NewsResponse> = newsApiService.getTopHeadlines(country, apiKey)

        call.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    val newsResponse = response.body()
                    _newsArticles.value = newsResponse?.articles ?: emptyList()
                } else {
                    // Handle error
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                // Handle failure
            }
        })
    }
}
