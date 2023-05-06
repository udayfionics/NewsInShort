package udayfionics.news.framework

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import udayfionics.core.data.News
import udayfionics.core.repository.NewsRepository
import udayfionics.core.usecase.DeleteAllNews
import udayfionics.core.usecase.GetAllNews
import udayfionics.core.usecase.GetNews
import udayfionics.core.usecase.InsertAllNews

class NewsDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val coroutineScopeIO = CoroutineScope(Dispatchers.IO)

    private val repository = NewsRepository(RoomNewsDataSource(application))

    private val useCases =
        UseCases(
            InsertAllNews(repository),
            GetNews(repository),
            GetAllNews(repository),
            DeleteAllNews(repository)
        )

    val news = MutableLiveData<News>()

    fun fetch(uuid: Long) {
        coroutineScopeIO.launch {
            val newsInfo = useCases.getNews(uuid)
            news.postValue(newsInfo!!)
        }
    }
}