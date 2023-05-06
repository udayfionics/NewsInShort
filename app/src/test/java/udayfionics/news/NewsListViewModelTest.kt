package udayfionics.news

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import udayfionics.core.data.News
import udayfionics.news.framework.remote.NewsApi
import udayfionics.news.framework.remote.NewsApiService
import udayfionics.news.framework.remote.NewsRemote
import udayfionics.news.framework.viewmodel.NewsListViewModel
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class NewsListViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private var testSingle: Single<NewsRemote>? = null

    @Mock
    lateinit var application: Application

    @Mock
    lateinit var apiService: NewsApiService

    @InjectMocks
    lateinit var newsListViewModel: NewsListViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        newsListViewModel = NewsListViewModel(application)
    }

    @Test
    fun getNewsFromRemoteSuccess() {
        val news1 = News("News Tittle", "News Content", "URL", 1)
        val newsData = arrayListOf(news1)
        val newsRemote = NewsRemote("science", newsData)

        testSingle = Single.just(newsRemote)
        Mockito.`when`(apiService.getRemoteNews()).thenReturn(testSingle)
        newsListViewModel.refresh()

        Assert.assertEquals(25, newsListViewModel.newsList.value?.size)
        Assert.assertEquals(false, newsListViewModel.loading.value)
        Assert.assertEquals(false, newsListViewModel.error.value)
    }

    @Test
    fun getNewsFromRemoteFailed() {
        testSingle = Single.error(Throwable("Error"))
        Mockito.`when`(apiService.getRemoteNews()).thenReturn(testSingle)
        newsListViewModel.refresh()

        Assert.assertEquals(null, newsListViewModel.newsList.value?.size)
        Assert.assertEquals(false, newsListViewModel.loading.value)
        Assert.assertEquals(true, newsListViewModel.error.value)
    }
    @Before
    fun setUpRxSchedulers() {
        val immediate = object : Scheduler() {
            override fun scheduleDirect(run: Runnable, delay: Long, unit: TimeUnit): Disposable {
                return super.scheduleDirect(run, 0, unit)
            }

            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() }, false)
            }
        }

        RxJavaPlugins.setInitIoSchedulerHandler { immediate }
        RxJavaPlugins.setInitComputationSchedulerHandler { immediate }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { immediate }
        RxJavaPlugins.setInitSingleSchedulerHandler { immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { immediate }
    }
}