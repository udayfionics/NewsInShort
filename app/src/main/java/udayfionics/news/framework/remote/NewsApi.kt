package udayfionics.news.framework.remote

import io.reactivex.Single
import retrofit2.http.GET

interface NewsApi {

    @GET("https://inshorts.deta.dev/news?category=technology")
    fun getNews(): Single<NewsRemote>
}