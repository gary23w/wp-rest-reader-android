package com.gary.news.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.gary.news.model.*


class DetailViewModel : ViewModel() {
    val newsArticlesComments = NewsRepository().getChats().asLiveData()
}

