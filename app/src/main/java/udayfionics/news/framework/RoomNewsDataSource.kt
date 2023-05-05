package udayfionics.news.framework

import android.content.Context
import udayfionics.core.data.News
import udayfionics.core.repository.NewsDataSource
import udayfionics.news.framework.db.NewsDBService

class RoomNewsDataSource(context: Context) : NewsDataSource {
    private val newsDao = NewsDBService.getInstance(context).newsDao()

    override suspend fun get(id: Long): News? = newsDao.getNewsEntity(id).toNews()

    override suspend fun getAll(): List<News> = newsDao.getAllNewsEntities().map { it.toNews() }
}