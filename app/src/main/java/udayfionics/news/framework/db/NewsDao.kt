package udayfionics.news.framework.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NewsDao {
    @Insert
    suspend fun insertAllNewsEntities(vararg newsEntity: NewsEntity): List<Long>

    @Query("SELECT * FROM news")
    suspend fun getAllNewsEntities(): List<NewsEntity>

    @Query("SELECT * FROM news WHERE id = :id")
    suspend fun getNewsEntity(id: Long): NewsEntity

    @Query("DELETE FROM news")
    suspend fun deleteAllNewsEntities()
}