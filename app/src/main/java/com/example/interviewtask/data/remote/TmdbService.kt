package com.example.interviewtask.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface TmdbService {

    @GET("3/discover/movie?sort_by=popularity.desc")
    suspend fun getPopularMovies(
        @Query("page") page: Int
    ): Response<MovieResponseDto>

}