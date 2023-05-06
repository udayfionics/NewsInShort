package udayfionics.core.usecase

import udayfionics.core.data.News
import udayfionics.core.repository.NewsRepository

class InsertAllNews(private val repository: NewsRepository) {
    suspend operator fun invoke(vararg news: News) = repository.insertAllNews(*news)
}