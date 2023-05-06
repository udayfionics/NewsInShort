package udayfionics.core.repository

import udayfionics.core.data.News

class NewsRepository(private val dataSource: NewsDataSource) {
    suspend fun insertAllNews(vararg news: News) = dataSource.insertAllNews(*news)

    suspend fun getNews(uuid: Long) = dataSource.get(uuid)

    suspend fun getAllNews() = dataSource.getAll()

    suspend fun deleteAllNews() = dataSource.deleteAllNews()
}