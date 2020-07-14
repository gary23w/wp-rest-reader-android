package com.gary.news.model

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.gary.news.model.dao.FavoriteDao
import com.gary.news.model.db.FavoriteDatabase

class FavoriteRepository(application: Application) {
        private var favoriteDao: FavoriteDao? = null
        var allFavsList : LiveData<List<Favorite>>
        private val favsDb: FavoriteDatabase
        init {
            favsDb = FavoriteDatabase.getNotePadDB(application)
            allFavsList = favsDb.favoriteDao().getAllNotes()
        }
        fun getNotesList() : LiveData<List<Favorite>> {
            return allFavsList
        }
        fun addNotes(favorite: Favorite) {
            addAsyncTask(favsDb).execute(favorite)
        }
        class addAsyncTask(favsDB: FavoriteDatabase): AsyncTask<Favorite, Void, Void>() {
            private var favoriteDB = favsDB

            override fun doInBackground(vararg params: Favorite): Void? {
                favoriteDB.favoriteDao().insertNote(params[0])
                return null
            }
        }
    }