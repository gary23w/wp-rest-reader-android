package com.gary.news.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.gary.news.model.Favorite
import com.gary.news.model.FavoriteRepository


class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
        private var favList : LiveData<List<Favorite>>
        private val favoriteRepo: FavoriteRepository
        init {
            favoriteRepo = FavoriteRepository(application)
            favList = favoriteRepo.getNotesList()
        }
        fun getAllNotesList() : LiveData<List<Favorite>> {
            return favList
        }
        fun addNotes(favorite: Favorite) {
            favoriteRepo.addNotes(favorite)
        }

    }
