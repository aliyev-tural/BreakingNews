package com.example.breakingnews.api

import com.example.breakingnews.models.NewsResponse
import com.example.breakingnews.api.ResearchKey.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface NewsInterface {
    @GET("v2/top-headlines")
    suspend fun getHeadlines(
        @Query("country")
        countryCode: String="us",

        @Query("page")
        pageNumber:Int=1,

        @Query("apiKey")
        apiKey:String=API_KEY
    ):Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q")
        searchQuery:String,

        @Query("page")
        pageNumber:Int=1,

        @Query("apiKey")
        apiKey:String= API_KEY
    ):Response<NewsResponse>

}