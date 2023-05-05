package udayfionics.core.usecase

import udayfionics.core.repository.NewsRepository

class GetAllNews(private val repository: NewsRepository) {
    suspend operator fun invoke() = repository.getAllNews()
}