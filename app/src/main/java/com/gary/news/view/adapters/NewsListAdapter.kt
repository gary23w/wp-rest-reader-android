package com.gary.news.view.adapters



import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gary.news.R
import com.gary.news.model.NewsArticle
import com.gary.news.utils.constant
import com.gary.news.utils.convertHtml
import com.gary.news.view.ArticleActivity
import com.gary.news.utils.loadImage
import kotlinx.android.synthetic.main.item_news_article.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class NewsListAdapter: RecyclerView.Adapter<NewsListAdapter.NewsItemViewHolder>(){
    val newsItems = arrayListOf<NewsArticle>()
    fun onAddNewsItem(item: NewsArticle) {
        newsItems.add(0, item)
        notifyItemInserted(0)
    }
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) =
        NewsItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_news_article, parent, false)
        )
    override fun getItemCount() = newsItems.size
    override fun onBindViewHolder(holder: NewsItemViewHolder, position: Int) {
        holder.bind(newsItems[position])
    }
    class NewsItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val imageView = view.newsImage
        private val title = view.newsTitle
        private val publishedAt = view.newsPublishedAt
//        private val excerpting = view.newsTitle1

        fun bind(newsItem: NewsArticle) {
            var input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            imageView.loadImage(newsItem.urlToImage)
            title.text = convertHtml(newsItem.title?.renderedTitle.toString())
            publishedAt.text = input.parse(newsItem.date).toString()

            itemView.setOnClickListener {
                constant.ID = newsItem.id.toString()
                constant.link = newsItem.link.toString()
                val context= itemView.context
                val intent = Intent( context, ArticleActivity::class.java)
                intent.putExtra("title",
                    convertHtml(newsItem.title?.renderedTitle.toString())
                )
                intent.putExtra("content",
                    convertHtml(newsItem.content?.renderedArticle.toString())
                )
                intent.putExtra("imageUrl", newsItem.urlToImage.toString())
                intent.putExtra("date", publishedAt.text.toString())
                context.startActivity(intent)
        }
        }

    }
}
