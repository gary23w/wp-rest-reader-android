package com.gary.news.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.gary.news.model.NewsRepository

class ListViewModel: ViewModel() {
    val newsArticles = NewsRepository().getNewsArticles().asLiveData()
    val trendingArticles = NewsRepository().getTrendingStuff().asLiveData()
    val generalArticles = NewsRepository().getGeneralStuff().asLiveData()
    val businessArticles = NewsRepository().getBusinessStuff().asLiveData()
    val scienceArticles = NewsRepository().getScience().asLiveData()
    val technologyArticles = NewsRepository().getTechnologyStuff().asLiveData()
    val searchArticles = NewsRepository().getSearching().asLiveData()
}