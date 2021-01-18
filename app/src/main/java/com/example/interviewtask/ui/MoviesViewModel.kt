package com.example.interviewtask.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.interviewtask.ui.entity.Movie
import com.example.interviewtask.usecase.GetPopularMoviesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MoviesViewModel @ViewModelInject constructor(
    private val popularMoviesUseCase: GetPopularMoviesUseCase
) : ViewModel() {

    private val _popularMovies = MutableStateFlow<PagingData<Movie>>(PagingData.empty())
    val popularMovies: StateFlow<PagingData<Movie>>
        get() = _popularMovies


    lateinit var selectedItem: MutableStateFlow<Movie>

    init {
        getTrendingMovies()
    }

    private fun getTrendingMovies() {
        viewModelScope.launch {
            popularMoviesUseCase().cachedIn(viewModelScope).collectLatest {
                _popularMovies.emit(it)
            }
        }
    }

    fun isSelectedItemInitialized() = this::selectedItem.isInitialized
}