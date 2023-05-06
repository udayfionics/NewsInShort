package udayfionics.news.framework.remote

import io.reactivex.Single
import udayfionics.news.framework.di.DaggerApiComponent
import javax.inject.Inject

class NewsApiService {

    @Inject
    lateinit var api: NewsApi

    init {
        DaggerApiComponent.create().inject(this)
    }

    fun getRemoteNews(): Single<NewsRemote> {
        return api.getNews()
    }
}