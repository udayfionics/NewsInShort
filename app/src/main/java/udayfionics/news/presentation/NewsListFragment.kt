package udayfionics.news.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import udayfionics.news.databinding.FragmentNewsListBinding
import udayfionics.news.framework.NewsListViewModel

class NewsListFragment : Fragment(), NewsItemClick {
    private lateinit var binding: FragmentNewsListBinding
    private lateinit var viewModel: NewsListViewModel
    private val listAdapter = NewsListAdapter(arrayListOf(), this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[NewsListViewModel::class.java]
        binding.listView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }
        observeViewModel()
        viewModel.fetchFromDatabase()
    }

    private fun observeViewModel() {
        viewModel.newsList.observe(viewLifecycleOwner, Observer {
            binding.listView.visibility = View.VISIBLE
            binding.loadingView.visibility = View.GONE
            binding.errorTextView.visibility = View.GONE
            listAdapter.updateList(it)
        })
        viewModel.loading.observe(viewLifecycleOwner, Observer {
            binding.loadingView.visibility = if (it) View.VISIBLE else View.GONE
            if (it) {
                binding.errorTextView.visibility = View.GONE
                binding.listView.visibility = View.GONE
            }
        })
        viewModel.error.observe(viewLifecycleOwner, Observer {
            binding.errorTextView.visibility = if (it) View.VISIBLE else View.GONE
            if (it) {
                binding.loadingView.visibility = View.GONE
                binding.listView.visibility = View.GONE
            }
        })
    }

    override fun onNewsItemClicked(id: Long) {
        val action = NewsListFragmentDirections.actionToNewsDetailFragment()
        action.newsId = id
        Navigation.findNavController(binding.listView).navigate(action)
    }
}