package udayfionics.core.repository

import udayfionics.core.data.News

interface NewsDataSource {
    suspend fun get(id: Long): News?

    suspend fun getAll(): List<News>
}