package udayfionics.news.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import udayfionics.core.data.News
import udayfionics.news.databinding.ItemNewsBinding

class NewsListAdapter(private var list: ArrayList<News>, private val click: NewsItemClick) :
    Adapter<NewsListAdapter.NewsListViewHolder>() {

    fun updateList(newList: List<News>) {
        val existingDogsCount = list.size
        list.clear()
        notifyItemRangeRemoved(0, existingDogsCount)
        list.addAll(newList)
        notifyItemRangeInserted(0, list.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NewsListViewHolder(
        ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: NewsListViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class NewsListViewHolder(private var binding: ItemNewsBinding) :
        ViewHolder(binding.root) {
        fun bind(news: News) {
            binding.titleView.text = news.title
            binding.contentView.text = news.content
            binding.root.setOnClickListener { click.onNewsItemClicked(news.uuid) }
        }
    }
}