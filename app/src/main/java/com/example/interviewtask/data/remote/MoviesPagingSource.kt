package com.example.interviewtask.data.remote

import androidx.paging.PagingSource
import retrofit2.HttpException
import java.io.IOException

class MoviesPagingSource(private val apiService: TmdbService): PagingSource<Int, MovieDto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDto> {
        return try {
            val apiResponse = apiService.getPopularMovies(page = params.key?: 1)
            if(apiResponse.isSuccessful) {
                LoadResult.Page(
                    data = apiResponse.body()?.results ?: emptyList(),
                    prevKey = apiResponse.body()?.page?.minus(1),
                    nextKey = apiResponse.body()?.page?.plus(1)
                )
            }else{
                LoadResult.Error(Exception("An unknown error occurred. Check your internet connection."))
            }
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}