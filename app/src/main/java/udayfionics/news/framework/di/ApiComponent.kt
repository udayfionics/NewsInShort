package udayfionics.news.framework.di

import dagger.Component
import udayfionics.news.framework.remote.NewsApiService
import udayfionics.news.framework.viewmodel.NewsDetailViewModel
import udayfionics.news.framework.viewmodel.NewsListViewModel

@Component(modules = [ApiModule::class, ApiServiceModule::class])
interface ApiComponent {
    fun inject(newsApiService: NewsApiService)
}