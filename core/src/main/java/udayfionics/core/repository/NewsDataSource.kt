package udayfionics.core.repository

import udayfionics.core.data.News

interface NewsDataSource {

    suspend fun insertAllNews(vararg news: News): List<Long>

    suspend fun get(uuid: Long): News?

    suspend fun getAll(): List<News>

    suspend fun deleteAllNews()
}