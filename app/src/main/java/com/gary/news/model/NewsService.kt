package com.gary.news.model

import retrofit2.Call
import retrofit2.http.*

interface NewsService {
    @GET("/wp-json/wp/v2/posts")
    suspend fun getNews(): List <NewsArticle>
    @GET("/wp-json/wp/v2/posts?page=2")
    suspend fun getTrending(): List <NewsArticle>
    @GET("/wp-json/wp/v2/posts?categories=2788")
    suspend fun getGeneral(): List <NewsArticle>
    @GET("/wp-json/wp/v2/posts?categories=3932")
    suspend fun getBusiness(): List <NewsArticle>
    @GET("/wp-json/wp/v2/posts?categories=3933")
    suspend fun getScience(): List <NewsArticle>
    @GET("/wp-json/wp/v2/posts?categories=3934")
    suspend fun getTechnology(): List <NewsArticle>
//    @GET("/wp-json/wp/v2/categories")
////    suspend fun getTopics(
////        @Query("page")
////        pageNumber: Int?
////    ): List <Topics>
    @GET("/wp-json/wp/v2/comments/")
    suspend fun getChatsTest(
//        @Query("post") articleID: String
    ): List <ChatResponse>
    @FormUrlEncoded
    @POST("/wp-json/wp/v2/comments")
    fun submitComment(
        @Field("author_email") authorEmail:String,
        @Field ("author_name")AuthorNameOne: String,
        @Field ("author_name")AuthorNameTwo: String,
        @Field ("content")Content: String,
        @Field ("post")Post: String
    ):
            Call<ChatResponse>
    @GET("/wp-json/wp/v2/posts")
    suspend fun searchStuff(@Query("search") searchingStuff:String?): List<NewsArticle>
}
