package udayfionics.news.framework.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import udayfionics.core.data.News

@Entity(tableName = "news")
data class NewsEntity(
    var title: String,
    var content: String,
    @ColumnInfo(name = "image_url")
    var imageUrl: String,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
) {
    companion object {
        fun fromNews(news: News) = NewsEntity(news.title, news.content, news.imageUrl, news.id)
    }

    fun toNews() = News(title, content, imageUrl, id)
}
