package udayfionics.news.framework.remote

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class NewsApiService {
    private val BASE_URL = "https://inshorts.deta.dev"

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(NewsApi::class.java)

    fun getRemoteNews(): Single<NewsRemote> {
        return api.getNews()
    }
}