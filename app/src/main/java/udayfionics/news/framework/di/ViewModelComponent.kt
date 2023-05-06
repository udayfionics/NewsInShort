package udayfionics.news.framework.di

import dagger.Component
import udayfionics.news.framework.di.api.ApiModule
import udayfionics.news.framework.di.api.ApiServiceModule
import udayfionics.news.framework.di.dispatcher.DispatcherModule
import udayfionics.news.framework.viewmodel.NewsDetailViewModel
import udayfionics.news.framework.viewmodel.NewsListViewModel

@Component(modules = [ApplicationModule::class, RepositoryModule::class, UseCasesModule::class, ApiModule::class, ApiServiceModule::class, DispatcherModule::class])
interface ViewModelComponent {
    fun inject(newsDetailViewModel: NewsDetailViewModel)

    fun inject(newsListViewModel: NewsListViewModel)
}