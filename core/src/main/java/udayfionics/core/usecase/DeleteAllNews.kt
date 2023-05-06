package udayfionics.core.usecase

import udayfionics.core.repository.NewsRepository

class DeleteAllNews(private val repository: NewsRepository) {
    suspend operator fun invoke() = repository.deleteAllNews()
}