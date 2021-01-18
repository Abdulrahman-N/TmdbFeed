package com.example.interviewtask.ui.movieDetails

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import coil.load
import com.example.interviewtask.R
import com.example.interviewtask.base.BaseFragment
import com.example.interviewtask.databinding.FragmentMovieDetailsBinding
import com.example.interviewtask.ui.entity.Movie
import com.example.interviewtask.ui.MoviesViewModel
import com.example.interviewtask.utils.appendAsUrl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MovieDetailsFragment :
    BaseFragment<FragmentMovieDetailsBinding>(R.layout.fragment_movie_details) {

    private val viewModel: MoviesViewModel by navGraphViewModels(R.id.main_graph) {
        defaultViewModelProviderFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeMovie()

    }

    private fun observeMovie() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.selectedItem.collectLatest {
                setupView(it)
            }
        }
    }

    private fun setupView(item: Movie) {
        binding.toolbar.title = item.title
        binding.bannerImage.load(item.banner.appendAsUrl())
        binding.overview.text = item.overview
    }

    override fun bindView(view: View) {
        _binding = FragmentMovieDetailsBinding.bind(view)
    }
}