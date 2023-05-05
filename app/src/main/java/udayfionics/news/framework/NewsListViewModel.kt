package udayfionics.news.framework

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import udayfionics.core.data.News
import udayfionics.core.repository.NewsRepository
import udayfionics.core.usecase.GetAllNews
import udayfionics.core.usecase.GetNews

class NewsListViewModel(application: Application) : AndroidViewModel(application) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val repository = NewsRepository(RoomNewsDataSource(application))

    private val useCases = UseCases(GetNews(repository), GetAllNews(repository))

    val newsList = MutableLiveData<List<News>>()
    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<Boolean>()

    fun refresh() {
        fetchFromDatabase()
    }

    fun fetchFromDatabase() {
        loading.value = true
        coroutineScope.launch {
            val newsAll = useCases.getAllNews()
            newsLoaded(newsAll)
        }
    }

    private fun newsLoaded(newsAll: List<News>) {
        newsList.postValue(newsAll)
        loading.postValue(false)
        error.postValue(false)
    }
}