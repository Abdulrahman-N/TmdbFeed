package com.example.interviewtask.repository

import androidx.paging.PagingData
import com.example.interviewtask.data.remote.MovieDto
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    suspend fun getTrendingMovies(): Flow<PagingData<MovieDto>>
}