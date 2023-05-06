package udayfionics.news.framework.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import udayfionics.core.data.News
import udayfionics.news.framework.UseCases
import udayfionics.news.framework.di.ApplicationModule
import udayfionics.news.framework.di.DaggerViewModelComponent
import udayfionics.news.framework.di.dispatcher.IoDispatcher
import javax.inject.Inject

class NewsDetailViewModel(application: Application) : AndroidViewModel(application) {

    @IoDispatcher
    @Inject
    lateinit var ioDispatcher: CoroutineDispatcher

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
        viewModelScope.launch(ioDispatcher) {
            val newsInfo = useCases.getNews(uuid)
            news.postValue(newsInfo!!)
        }
    }
}