package com.gary.news.model


import com.gary.news.utils.constant
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsRepository {
    companion object{
        private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .method(original.method(), original.body())
                val request = requestBuilder.build()
                chain.proceed(request)
            }.build()
        val instance: NewsService by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
            retrofit.create(NewsService::class.java)
        }

        private const val BASE_URL = "https://the-latest.news"
        private const val NEWS_DELAY = 100L
    }
    val newsService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(NewsService::class.java)
    fun getChats() : Flow<ChatResponse> {
        return flow {
            var chatSource = newsService.getChatsTest()
            chatSource.forEach {
                emit(it)
                delay(NEWS_DELAY)
            }
        }
    }
//    fun getTopics() : Flow<Topics> {
//        return flow {
//            var newsource = newsService.getTopics(1)
//            newsource.forEach {
//                emit(it)
//            }
//        }
//    }
    fun getNewsArticles() : Flow<NewsArticle> {
        return flow {
            var newsource = newsService.getNews()
            newsource.forEach {
                emit(it)
            }
        }
    }
    fun getTrendingStuff() : Flow<NewsArticle> {
        return flow {
            var newsource = newsService.getTrending()
            newsource.forEach {
                emit(it)
            }
        }
    }
    fun getGeneralStuff() : Flow<NewsArticle> {
        return flow {
            var newsource = newsService.getGeneral()
            newsource.forEach {
                emit(it)
            }
        }
    }
    fun getBusinessStuff() : Flow<NewsArticle> {
        return flow {
            var newsource = newsService.getBusiness()
            newsource.forEach {
                emit(it)
            }
        }
    }
    fun getScience() : Flow<NewsArticle> {
        return flow {
            var newsource = newsService.getScience()
            newsource.forEach {
                emit(it)
            }
        }
    }
    fun getTechnologyStuff() : Flow<NewsArticle> {
        return flow {
            var newsource = newsService.getTechnology()
            newsource.forEach {
                emit(it)
            }
        }
    }
    fun getSearching() : Flow<NewsArticle> {
        return flow {
            var newsource = newsService.searchStuff(constant.SEARCH)
            newsource.forEach {
                emit(it)
            }
        }
    }
}
