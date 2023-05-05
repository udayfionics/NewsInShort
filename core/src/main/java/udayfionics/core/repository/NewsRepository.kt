package udayfionics.core.repository

class NewsRepository(private val dataSource: NewsDataSource) {
    suspend fun getNews(id: Long) = dataSource.get(id)

    suspend fun getAllNews() = dataSource.getAll()
}