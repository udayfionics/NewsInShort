package udayfionics.news.framework.di

import dagger.Component
import udayfionics.news.framework.viewmodel.NewsDetailViewModel
import udayfionics.news.framework.viewmodel.NewsListViewModel

@Component(modules = [ApplicationModule::class, RepositoryModule::class, UseCasesModule::class])
interface ViewModelComponent {
    fun inject(newsDetailViewModel: NewsDetailViewModel)

    fun inject(newsListViewModel: NewsListViewModel)
}