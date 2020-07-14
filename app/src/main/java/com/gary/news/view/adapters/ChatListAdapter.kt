package com.gary.news.view.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gary.news.R
import com.gary.news.model.ChatResponse
import com.gary.news.utils.constant
import com.gary.news.utils.convertHtml
import com.gary.news.view.CommentExpand
import kotlinx.android.synthetic.main.item_chat.view.*
import kotlinx.android.synthetic.main.item_news_article.view.*
import kotlinx.android.synthetic.main.item_news_article.view.newsTitle


class ChatList: RecyclerView.Adapter<ChatList.ChatItemViewHolder>(){
    val chat = arrayListOf<ChatResponse>()
    fun onAddNewsItem(item: ChatResponse) {
        chat.add(0, item)
        notifyItemInserted(0)
        chat.reverse()
    }
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) =
        ChatItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        )
    override fun getItemCount() = chat.size
    override fun onBindViewHolder(holder: ChatItemViewHolder, position: Int) {
        holder.bind(chat[position])
    }
    class ChatItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val title = view.newsTitle
        private val user = view.newsAuthor
        private val userid = view.userIdChat
        fun bind(chatItem: ChatResponse) {
            title.text = convertHtml(chatItem.content?.renderedPost.toString())
            user.text = chatItem.authorName
            userid.text = chatItem.idSort.toString()
                itemView.setOnClickListener {
                val context= itemView.context
                val intent = Intent( context, CommentExpand::class.java)
                intent.putExtra("author", chatItem.authorName.toString())
                intent.putExtra("content", convertHtml(chatItem.content?.renderedPost.toString()))
                intent.putExtra("post", chatItem.link.toString())
                    context.startActivity(intent)
            }
        }
    }
}