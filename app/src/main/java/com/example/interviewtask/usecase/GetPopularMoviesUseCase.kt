package com.example.interviewtask.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.example.interviewtask.repository.MoviesRepositoryImpl
import com.example.interviewtask.ui.entity.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetPopularMoviesUseCase @Inject constructor(private val repositoryImpl: MoviesRepositoryImpl) {

    suspend operator fun invoke(): Flow<PagingData<Movie>> {
        return repositoryImpl.getTrendingMovies().map { pagingData ->
            pagingData.map { movieDto ->
                Movie(
                    id = movieDto.id!!,
                    title = movieDto.title ?: "",
                    poster = movieDto.poster_path ?: "",
                    banner = movieDto.backdrop_path ?: "",
                    overview = movieDto.overview ?: ""
                )
            }
        }
    }
}