package udayfionics.core.usecase

import udayfionics.core.repository.NewsRepository

class GetNews(private val repository: NewsRepository) {
    suspend operator fun invoke(uuid: Long) = repository.getNews(uuid)
}