package com.gary.news.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gary.news.model.Favorite
import com.gary.news.model.dao.FavoriteDao


@Database(entities = [(Favorite::class)], version = 1, exportSchema = false)
    abstract class FavoriteDatabase : RoomDatabase() {

        abstract fun favoriteDao() : FavoriteDao

        companion object {
            private var INSTANCE : FavoriteDatabase? = null
            fun getNotePadDB(context: Context) : FavoriteDatabase {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, FavoriteDatabase::class.java, "favorite-db")
                        .allowMainThreadQueries().build()
                }
                return INSTANCE as FavoriteDatabase
            }
        }

}