package udayfionics.news.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import udayfionics.news.databinding.FragmentNewsDetailBinding
import udayfionics.news.framework.viewmodel.NewsDetailViewModel
import udayfionics.news.util.getProgressDrawable
import udayfionics.news.util.loadImage

class NewsDetailFragment : Fragment() {
    private lateinit var binding: FragmentNewsDetailBinding
    private lateinit var viewModel: NewsDetailViewModel
    private var newsUuid: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            newsUuid = NewsDetailFragmentArgs.fromBundle(it).newsId
        }
        viewModel = ViewModelProvider(this)[NewsDetailViewModel::class.java]
        viewModel.fetch(newsUuid)

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.news.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.titleView.text = it.title
                binding.contentView.text = it.content
                binding.imageView.loadImage(
                    it.imageUrl,
                    getProgressDrawable(binding.imageView.context)
                )
            }
        })
    }
}