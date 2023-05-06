package udayfionics.news.framework.di.api

import dagger.Component
import udayfionics.news.framework.remote.NewsApiService

@Component(modules = [ApiModule::class, ApiServiceModule::class])
interface ApiComponent {
    fun inject(newsApiService: NewsApiService)
}