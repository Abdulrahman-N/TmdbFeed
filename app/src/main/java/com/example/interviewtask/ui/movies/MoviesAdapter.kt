package com.example.interviewtask.ui.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import coil.load
import com.example.interviewtask.base.BasePagedAdapter
import com.example.interviewtask.base.BaseViewHolder
import com.example.interviewtask.databinding.ItemMovieBinding
import com.example.interviewtask.ui.entity.Movie
import com.example.interviewtask.utils.appendAsUrl

class PostsAdapter(private val postOnClick: (Int) -> Unit) : BasePagedAdapter<Movie>(object : DiffUtil.ItemCallback<Movie>() {

        override fun areItemsTheSame(oldItem: Movie, newItem: Movie) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie) = oldItem == newItem

    }) {

    override fun createView(viewType: Int, parent: ViewGroup): BaseViewHolder<Movie> {
            return MovieViewHolder.from(parent, postOnClick)
        }
}


class MovieViewHolder(
    private val binding: ItemMovieBinding,
    private val postOnClick: (Int) -> Unit
) : BaseViewHolder<Movie>(binding) {
    override fun bind(item: Movie) {
        binding.coverImage.load(item.poster.appendAsUrl())
        binding.title.text = item.title

        binding.root.setOnClickListener {
            postOnClick(absoluteAdapterPosition)
        }
    }

    companion object {
        fun from(
            parent: ViewGroup,
            postOnClick: (Int) -> Unit
        ): MovieViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemMovieBinding.inflate(layoutInflater, parent, false)
            return MovieViewHolder(binding, postOnClick)
        }
    }
}
