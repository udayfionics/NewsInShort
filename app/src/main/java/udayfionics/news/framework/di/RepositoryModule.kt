package udayfionics.news.framework.di

import android.app.Application
import dagger.Module
import dagger.Provides
import udayfionics.core.repository.NewsRepository
import udayfionics.news.framework.RoomNewsDataSource

@Module
class RepositoryModule {
    @Provides
    fun providesRepository(application: Application) =
        NewsRepository(RoomNewsDataSource(application))
}