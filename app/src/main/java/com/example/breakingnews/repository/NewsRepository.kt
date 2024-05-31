package com.example.breakingnews.repository

import com.example.breakingnews.api.RetrofitInstance
import com.example.breakingnews.database.ArticleDatabase
import com.example.breakingnews.models.Article

class NewsRepository(val db:ArticleDatabase) {

suspend fun getHeadlines(countryCode:String,pageNumber:Int)=
    RetrofitInstance.api.getHeadlines(countryCode, pageNumber)

    suspend fun searchNews(searchQuery:String,pageNumber: Int)=
        RetrofitInstance.api.searchNews(searchQuery, pageNumber)

    suspend fun insert(article: Article)=db.getArticleDao().insert(article)

    fun getFavoriteNews()=db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article)=db.getArticleDao().deleteArticle(article)
}
