package com.st10083770.myapplicationnewsapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var newsArticles: List<NewsArticle>
    private lateinit var newsApiService: NewsApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Retrofit with the News API base URL
        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Create an instance of the NewsApiService
        newsApiService = retrofit.create(NewsApiService::class.java)

        // Fetch news articles
        getNewsArticles()
    }

    private fun getNewsArticles() {
        val country = "us" // Replace with the desired country code
        val apiKey = "ab3dee3d075a4e4c82ef0f2ff4163bcd" // Replace with your actual API key

        val call: Call<NewsResponse> = newsApiService.getTopHeadlines(country, apiKey)

        call.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    val newsResponse = response.body()
                    newsArticles = newsResponse?.articles ?: emptyList()
                    setupRecyclerView()
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                // Handle failure here
            }
        })
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.newsRecyclerView)
        val adapter = NewsAdapter(newsArticles)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}
