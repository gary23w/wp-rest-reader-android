package com.gary.news.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favs")
data class Favorite(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "favID")
    var id: Int = 0,

    @ColumnInfo(name = "favText")
    var text: String = "",

    @ColumnInfo(name = "favDate")
    var date: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "imageUrl")
    var imageUrlFav: String = "",

    @ColumnInfo(name = "title")
    var articleTitle: String = ""
)
