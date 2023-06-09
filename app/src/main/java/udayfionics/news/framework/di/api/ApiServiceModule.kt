package udayfionics.news.framework.di.api

import dagger.Module
import dagger.Provides
import udayfionics.news.framework.remote.NewsApiService

@Module
class ApiServiceModule {
    @Provides
    fun providesNewsApiService() = NewsApiService()
}