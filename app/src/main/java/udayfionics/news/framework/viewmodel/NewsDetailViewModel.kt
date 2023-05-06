package udayfionics.news.framework.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import udayfionics.core.data.News
import udayfionics.news.framework.UseCases
import udayfionics.news.framework.di.ApplicationModule
import udayfionics.news.framework.di.DaggerViewModelComponent
import javax.inject.Inject

class NewsDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val coroutineScopeIO = CoroutineScope(Dispatchers.IO)

    @Inject
    lateinit var useCases: UseCases

    init {
        DaggerViewModelComponent.builder()
            .applicationModule(ApplicationModule(application))
            .build()
            .inject(this)
    }

    val news = MutableLiveData<News>()

    fun fetch(uuid: Long) {
        coroutineScopeIO.launch {
            val newsInfo = useCases.getNews(uuid)
            news.postValue(newsInfo!!)
        }
    }
}