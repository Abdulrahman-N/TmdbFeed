package com.example.interviewtask.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class MovieResponseDto(
     val page: Int?,
     val results: List<MovieDto>?,
     val total_pages: Int?,
     val total_results: Int?
)

@Serializable
data class MovieDto(
     val id: Int? = null,
     val title: String? = null,
     val poster_path: String?= null,
     val overview: String? = null,
     val backdrop_path: String? = null
)