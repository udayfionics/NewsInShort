package udayfionics.news.framework

import udayfionics.core.usecase.DeleteAllNews
import udayfionics.core.usecase.GetAllNews
import udayfionics.core.usecase.GetNews
import udayfionics.core.usecase.InsertAllNews

data class UseCases(
    val insertAllNews: InsertAllNews,
    val getNews: GetNews,
    val getAllNews: GetAllNews,
    val deleteAllNews: DeleteAllNews
)
