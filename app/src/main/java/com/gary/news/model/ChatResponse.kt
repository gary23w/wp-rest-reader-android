package com.gary.news.model

import com.google.gson.annotations.SerializedName

data class ChatResponse(
    @SerializedName("id")
    val idSort: Int,
    @SerializedName("author_name")
    val authorName: String? = null,
    @SerializedName("content")
    val content: Post? = null,
    @SerializedName("link")
    val link: String? = null
)
data class Post (
    @SerializedName("rendered")
    val renderedPost: String? = null)