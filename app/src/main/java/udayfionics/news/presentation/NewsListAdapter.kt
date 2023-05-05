package udayfionics.news.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import udayfionics.core.data.News
import udayfionics.news.databinding.ItemNewsBinding

class NewsListAdapter(private var newsList: List<News>) :
    Adapter<NewsListAdapter.NewsListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NewsListViewHolder(
        ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount() = newsList.size

    override fun onBindViewHolder(holder: NewsListViewHolder, position: Int) {
        holder.bind(newsList[position])
    }

    inner class NewsListViewHolder(private var binding: ItemNewsBinding) :
        ViewHolder(binding.root) {
        fun bind(news: News) {
            binding.titleView.text = news.title
            binding.contentView.text = news.content
        }
    }
}