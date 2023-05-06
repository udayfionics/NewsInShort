package udayfionics.news.framework.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [NewsEntity::class], version = 1, exportSchema = false)
abstract class NewsDBService : RoomDatabase() {
    abstract fun newsDao(): NewsDao

    companion object {
        private const val DATABASE_NAME = "news.db"
        private var dbService: NewsDBService? = null

        private fun create(context: Context): NewsDBService =
            Room.databaseBuilder(context, NewsDBService::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()

        fun getInstance(context: Context): NewsDBService =
            dbService ?: create(context).also { dbService = it }
    }
}