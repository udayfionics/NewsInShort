package udayfionics.core.usecase

import udayfionics.core.repository.NewsRepository

class GetNews(private val repository: NewsRepository) {
    suspend operator fun invoke(id: Long) = repository.getNews(id)
}