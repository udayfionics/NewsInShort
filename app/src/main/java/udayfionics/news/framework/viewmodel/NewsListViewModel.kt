package udayfionics.news.framework.viewmodel

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
import udayfionics.news.framework.RoomNewsDataSource
import udayfionics.news.framework.UseCases
import udayfionics.news.framework.remote.NewsApiService
import udayfionics.news.framework.remote.NewsRemote

class NewsListViewModel(application: Application) : AndroidViewModel(application) {

    private val coroutineScopeIO = CoroutineScope(Dispatchers.IO)
    private val coroutineScopeMain = CoroutineScope(Dispatchers.Main)

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
                        if (remote.data.isEmpty()) {
                            Toast.makeText(
                                getApplication(),
                                "No data available, Please try again",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                getApplication(),
                                "Fetched from Remote",
                                Toast.LENGTH_LONG
                            ).show()
                        }
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
        coroutineScopeIO.launch {
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
        coroutineScopeIO.launch {
            val newsAll = useCases.getAllNews()
            newsLoaded(newsAll)
            coroutineScopeMain.launch { fetchFromRemoteIfNoData() }
        }
    }

    private fun fetchFromRemoteIfNoData() {
        if (newsList.value != null && newsList.value!!.isNotEmpty()) {
            Toast.makeText(getApplication(), "Fetched from Database", Toast.LENGTH_LONG)
                .show()
        } else {
            refresh()
        }
    }

    private fun newsLoaded(newsAll: List<News>) {
        coroutineScopeMain.launch {
            newsList.value = newsAll
            loading.value = false
            error.value = false
        }
    }
}