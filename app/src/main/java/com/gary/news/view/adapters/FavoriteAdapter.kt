package com.gary.news.view.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gary.news.R
import com.gary.news.model.Favorite
import com.gary.news.model.Topics
import com.gary.news.utils.constant
import com.gary.news.utils.convertHtml
import com.gary.news.utils.loadImage
import com.gary.news.view.ArticleActivity
import com.gary.news.view.WebView
import kotlinx.android.synthetic.main.item_fav.view.*
import java.text.SimpleDateFormat

class FavoriteAdapter(favorite: ArrayList<Favorite>, listener: OnItemClickListener) : RecyclerView.Adapter<FavoriteAdapter.FavItemViewHolder>() {

        private var favList: List<Favorite> = favorite

        private var listenerContact: OnItemClickListener = listener

        interface OnItemClickListener {
            fun onItemClick(favorite: Favorite)
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavItemViewHolder {
            return FavItemViewHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.item_fav, parent, false))
        }
        override fun getItemCount(): Int {
            return favList.size
        }

        override fun onBindViewHolder(holder: FavItemViewHolder, position: Int) {
            var currentFav: Favorite = favList[position]
            holder.title.text = getDate(currentFav)
            holder.image.loadImage(currentFav.imageUrlFav)
            holder.date.text = currentFav.articleTitle
            holder.link.text = currentFav.text
            holder.bind(currentFav, listenerContact)
        }
        fun addNotes(listFav: List<Favorite>) {
            this.favList = listFav
            notifyDataSetChanged()
        }
        private fun getDate(currentDate: Favorite): String {
            var date: Long = currentDate.date
            return SimpleDateFormat("yyyy-MM-dd").format(date)
        }
        class FavItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var title = itemView.findViewById<TextView>(R.id.newsTitleFav)!!
            var date = itemView.findViewById<TextView>(R.id.newsFavDate)!!
            var image = itemView.findViewById<ImageView>(R.id.favoriteImage)!!
            var link = itemView.findViewById<TextView>(R.id.newsLink)!!
            fun bind(favorite: Favorite, listener: OnItemClickListener) {
                itemView.setOnClickListener {
                    constant.link = itemView.newsLink.text.toString()
                    val context= itemView.context
                    val intent = Intent( context, WebView::class.java)
                    context.startActivity(intent)
                }
            }

        }

    }