package udayfionics.news.framework.remote

import udayfionics.core.data.News

data class NewsRemote(
    var category: String,
    var data: List<News>
)
