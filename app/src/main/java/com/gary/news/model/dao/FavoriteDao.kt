package com.gary.news.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.gary.news.model.Favorite


@Dao
    interface FavoriteDao {
        @Query("select * from favs")
        fun getAllNotes() : LiveData<List<Favorite>>
        @Query("select * from favs where favID in (:id)")
        fun getNotesById(id: Int) : Favorite
        @Query("delete from favs")
        fun deleteAllNotes()
        @Insert(onConflict = OnConflictStrategy.ABORT)
        fun insertNote(favorite: Favorite)
        @Update
        fun updateNote(favorite: Favorite)
        @Delete
        fun deleteNote(favorite: Favorite)


}