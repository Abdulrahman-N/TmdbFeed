package com.example.interviewtask.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.interviewtask.data.remote.MoviesPagingSource
import com.example.interviewtask.data.remote.TmdbService
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(private val apiService: TmdbService) :
    MoviesRepository {


    override suspend fun getTrendingMovies() = withContext(IO) {
        Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { MoviesPagingSource(apiService) }
        ).flow
    }

}