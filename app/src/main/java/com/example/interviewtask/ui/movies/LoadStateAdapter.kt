package com.example.interviewtask.ui.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.interviewtask.databinding.ItemLoadStateBinding

class LoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<LoadStateViewHolder>() {
    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder.from(parent, retry)
    }
}

class LoadStateViewHolder(
    private val binding: ItemLoadStateBinding,
    private val retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryBtn.setOnClickListener { retry() }
    }

    fun bind(loadState: LoadState) {
        binding.apply {
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.retryBtn.isVisible = loadState !is LoadState.Loading
            binding.errorMessage.isVisible = loadState !is LoadState.Loading
            if (loadState is LoadState.Error) {
                errorMessage.text = loadState.error.localizedMessage
            }
        }
    }

    companion object {
        fun from(
            parent: ViewGroup,
            retry: () -> Unit
        ): LoadStateViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemLoadStateBinding.inflate(layoutInflater, parent, false)
            return LoadStateViewHolder(binding, retry)
        }
    }
}
