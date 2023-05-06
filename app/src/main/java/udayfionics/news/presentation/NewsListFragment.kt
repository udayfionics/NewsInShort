package udayfionics.news.presentation

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import udayfionics.news.R
import udayfionics.news.databinding.FragmentNewsListBinding
import udayfionics.news.framework.viewmodel.NewsListViewModel

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
        setUpMenu()

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

    private fun setUpMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.list_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.menuActionRefresh -> {
                        view?.let {
                            viewModel.refresh()
                        }
                        return true
                    }
                }
                return false
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}