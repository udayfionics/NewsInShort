package udayfionics.news.framework

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import udayfionics.core.data.News
import udayfionics.core.repository.NewsRepository
import udayfionics.core.usecase.DeleteAllNews
import udayfionics.core.usecase.GetAllNews
import udayfionics.core.usecase.GetNews
import udayfionics.core.usecase.InsertAllNews
import udayfionics.news.framework.remote.NewsApiService
import udayfionics.news.framework.remote.NewsRemote

class NewsListViewModel(application: Application) : AndroidViewModel(application) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val apiService = NewsApiService()

    private val disposable = CompositeDisposable()

    private val repository = NewsRepository(RoomNewsDataSource(application))

    private val useCases =
        UseCases(
            InsertAllNews(repository),
            GetNews(repository),
            GetAllNews(repository),
            DeleteAllNews(repository)
        )

    val newsList = MutableLiveData<List<News>>()
    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<Boolean>()

    fun refresh() {
//        fetchFromDatabase()
        fetchFromRemote()
    }

    private fun fetchFromRemote() {
        loading.value = true
        disposable.add(
            apiService.getRemoteNews()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<NewsRemote>() {
                    override fun onSuccess(remote: NewsRemote) {
                        storeNewsLocally(remote.data)
                        Toast.makeText(getApplication(), "Fetched from Remote", Toast.LENGTH_LONG).show()
                    }

                    override fun onError(e: Throwable) {
                        error.value = true
                        loading.value = false
                        Toast.makeText(getApplication(), e.message, Toast.LENGTH_LONG).show()
                        e.printStackTrace()
                    }
                })
        )
    }

    private fun storeNewsLocally(data: List<News>) {
        coroutineScope.launch {
            useCases.deleteAllNews()
            val result = useCases.insertAllNews(*data.toTypedArray())
            var i = 0
            while (i < data.size) {
                data[i].uuid = result[i]
                ++i
            }
            newsLoaded(data)
        }
    }

    fun fetchFromDatabase() {
        loading.value = true
        coroutineScope.launch {
            val newsAll = useCases.getAllNews()
            newsLoaded(newsAll)
        }
        Toast.makeText(getApplication(), "Fetched from Database", Toast.LENGTH_LONG).show()
    }

    private fun newsLoaded(newsAll: List<News>) {
        newsList.postValue(newsAll)
        loading.postValue(false)
        error.postValue(false)
    }
}