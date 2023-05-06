package udayfionics.news.framework.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import udayfionics.news.framework.remote.NewsApi

@Module
class ApiModule {
    private val BASE_URL = "https://inshorts.deta.dev"

    @Provides
    fun providesNewsApi() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(NewsApi::class.java)
}