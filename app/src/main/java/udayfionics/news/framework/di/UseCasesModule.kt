package udayfionics.news.framework.di

import dagger.Module
import dagger.Provides
import udayfionics.core.repository.NewsRepository
import udayfionics.core.usecase.DeleteAllNews
import udayfionics.core.usecase.GetAllNews
import udayfionics.core.usecase.GetNews
import udayfionics.core.usecase.InsertAllNews
import udayfionics.news.framework.UseCases

@Module
class UseCasesModule {
    @Provides
    fun providesUseCases(repository: NewsRepository) =
        UseCases(
            InsertAllNews(repository),
            GetNews(repository),
            GetAllNews(repository),
            DeleteAllNews(repository)
        )
}