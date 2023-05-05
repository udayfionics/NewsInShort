package udayfionics.news.framework

import udayfionics.core.usecase.GetAllNews
import udayfionics.core.usecase.GetNews

data class UseCases(
    val getNews: GetNews,
    val getAllNews: GetAllNews
)
