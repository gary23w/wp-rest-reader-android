package com.gary.news.model

import com.google.gson.annotations.SerializedName


data class NewsArticle(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("slug")
    val slug: String? = null,
    @SerializedName("content")
    val content: Article? = null,
    @SerializedName("title")
    val title: Title? = null,
    @SerializedName("excerpt")
    val exerpt: Excerpt? = null,
    @SerializedName("link")
    val link: String? = null,
    @SerializedName("date")
    val date: String? = null,
    @SerializedName("jetpack_featured_media_url")
    var urlToImage: String? = null
)
data class Title (
    @SerializedName("rendered")
    val renderedTitle: String? = null)
data class Article (
    @SerializedName("rendered")
    val renderedArticle: String? = null
)
data class Excerpt(
    @SerializedName("rendered")
    val renderedExcerpt: String? = null
)


