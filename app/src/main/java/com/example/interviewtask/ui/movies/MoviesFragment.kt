package com.example.interviewtask.ui.movies

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.interviewtask.R
import com.example.interviewtask.base.BaseFragment
import com.example.interviewtask.databinding.FragmentMoviesBinding
import com.example.interviewtask.ui.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MoviesFragment : BaseFragment<FragmentMoviesBinding>(R.layout.fragment_movies) {

    private val viewModel: MoviesViewModel by navGraphViewModels(R.id.main_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var adapter: PostsAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        observeTrendingMovies()
        refreshListener()
        observeAdapterState()
        setupToolbar()
    }

    private fun observeTrendingMovies() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.popularMovies.collectLatest { result ->
                adapter.submitData(result)
            }
        }
    }

    private fun refreshListener() {
        binding.refreshLayout.setOnRefreshListener {
            if (binding.refreshLayout.isRefreshing) {
                adapter.refresh()
                binding.refreshLayout.isRefreshing = false
            }
        }
    }

    private fun setupToolbar() {
        toggleDarkModeIcon(false)
        binding.toolbar.menu.findItem(R.id.item_darkMode).setOnMenuItemClickListener {
            toggleDarkModeIcon(true)
            true
        }
    }

    private fun toggleDarkModeIcon(toggle: Boolean) {
        if (toggle) {
            if (ui.isDarkTheme()) {
                binding.toolbar.menu.findItem(R.id.item_darkMode).setIcon(R.drawable.ic_dark_mode_outlined)
                ui.setDarkTheme(false)
            } else {
                binding.toolbar.menu.findItem(R.id.item_darkMode).setIcon(R.drawable.ic_dark_mode_filled)
                ui.setDarkTheme(true)
            }
        } else {
            if (ui.isDarkTheme()) {
                binding.toolbar.menu.findItem(R.id.item_darkMode)
                    .setIcon(R.drawable.ic_dark_mode_filled)
            } else {
                binding.toolbar.menu.findItem(R.id.item_darkMode)
                    .setIcon(R.drawable.ic_dark_mode_outlined)
            }
        }
    }

    private fun setupRecycler() {
        adapter = PostsAdapter {
            if (viewModel.isSelectedItemInitialized()) {
                viewModel.selectedItem.value = adapter.peek(it)!!
            } else {
                viewModel.selectedItem = MutableStateFlow(adapter.peek(it)!!)
            }
            navigate(R.id.action_postsFragment_to_postDetailsFragment)
        }
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.recycler.adapter = adapter.withLoadStateFooter(LoadStateAdapter(adapter::retry))
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeAdapterState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            adapter.loadStateFlow.collectLatest { loadStates ->
                ui.showProgress(loadStates.refresh is LoadState.Loading)
            }
        }
    }

    override fun bindView(view: View) {
        _binding = FragmentMoviesBinding.bind(view)
    }
}