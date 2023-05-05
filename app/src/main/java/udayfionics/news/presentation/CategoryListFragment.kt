package udayfionics.news.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import udayfionics.news.databinding.FragmentCategoryListBinding

class CategoryListFragment : Fragment() {
    private lateinit var binding: FragmentCategoryListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCategoryListBinding.inflate(inflater, container, false)
        return binding.root
    }
}