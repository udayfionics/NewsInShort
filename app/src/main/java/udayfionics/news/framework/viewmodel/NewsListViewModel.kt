package udayfionics.news.framework.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import udayfionics.core.data.News
import udayfionics.news.framework.UseCases
import udayfionics.news.framework.di.ApplicationModule
import udayfionics.news.framework.di.DaggerViewModelComponent
import udayfionics.news.framework.di.dispatcher.IoDispatcher
import udayfionics.news.framework.di.dispatcher.MainDispatcher
import udayfionics.news.framework.remote.NewsApiService
import udayfionics.news.framework.remote.NewsRemote
import javax.inject.Inject

class NewsListViewModel(application: Application) : AndroidViewModel(application) {

    @IoDispatcher
    @Inject
    lateinit var ioDispatcher: CoroutineDispatcher

    @MainDispatcher
    @Inject
    lateinit var mainDispatcher: CoroutineDispatcher

    @Inject
    lateinit var apiService: NewsApiService

    private val disposable = CompositeDisposable()

    @Inject
    lateinit var useCases: UseCases

    init {
        DaggerViewModelComponent.builder()
            .applicationModule(ApplicationModule(application))
            .build()
            .inject(this)
    }

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
        viewModelScope.launch(ioDispatcher) {
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
        viewModelScope.launch(ioDispatcher) {
            val newsAll = useCases.getAllNews()
            newsLoaded(newsAll)
            viewModelScope.launch(mainDispatcher) { fetchFromRemoteIfNoData() }
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
        viewModelScope.launch(mainDispatcher) {
            newsList.value = newsAll
            loading.value = false
            error.value = false
        }
    }
}