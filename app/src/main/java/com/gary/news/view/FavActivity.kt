package com.gary.news.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.gary.news.R
import com.gary.news.model.Favorite
import com.gary.news.model.dao.FavoriteDao
import com.gary.news.model.db.FavoriteDatabase
import com.gary.news.view.adapters.FavoriteAdapter
import com.gary.news.viewModel.FavoriteViewModel
import kotlinx.android.synthetic.main.activity_fav.*

class FavActivity : BaseActivity(), FavoriteAdapter.OnItemClickListener {

    private lateinit var favoriteDatabase: FavoriteDatabase

    private lateinit var favoriteDao: FavoriteDao

    private lateinit var favListLive: LiveData<List<Favorite>>

    private lateinit var viewModel: FavoriteViewModel

    private var favoriteAdapter: FavoriteAdapter? = null

    private var favoriteList: List<Favorite>?  = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fav)
        setSupportActionBar(toolbar_favorite)
        val actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
        }
        toolbar_favorite.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        favoriteDatabase = FavoriteDatabase.getNotePadDB(this)
        favoriteDao = favoriteDatabase.favoriteDao()
        favListLive = favoriteDao.getAllNotes()

        favoriteAdapter = FavoriteAdapter(arrayListOf(), this)
        favList.apply {
            layoutManager = LinearLayoutManager(this@FavActivity)
            adapter = favoriteAdapter
        }
        viewModel = ViewModelProviders.of(this).get(FavoriteViewModel::class.java)
        viewModel.getAllNotesList().observe(
            this,
            Observer { favorites: List<Favorite>? ->
                favoriteList = favorites
                favoriteAdapter!!.addNotes(favorites!!)
            })
    }
    private fun deleteAllNotes() {
        favoriteDao.deleteAllNotes()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.fav_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_everything -> {
                deleteAllNotes()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onItemClick(favorite: Favorite) {
        //delete single on hold
    }
}