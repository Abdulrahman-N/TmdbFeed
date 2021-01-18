package com.example.interviewtask.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.interviewtask.data.remote.TmdbService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit

class TmdbServiceTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var service: TmdbService

    private lateinit var mockWebServer: MockWebServer

    @ExperimentalSerializationApi
    @Before
    fun setupService(){
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(Json {
                ignoreUnknownKeys = true
            }.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(TmdbService::class.java)
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }

    // Testing which Simulate getting a response from the Api
    @Test
    fun getMovies() = runBlocking {
        enqueueResponse("movies.json")
        val response = service.getPopularMovies(1)
        val movies = response.body()?.results
        assertThat(movies?.size, `is`(20))
        val movie = movies?.get(0)
        assertThat(movie?.title, `is`("Wonder Woman 1984"))
        assertThat(movie?.poster_path, `is`("/8UlWHLMpgZm9bx6QYh0NFoq67TZ.jpg"))
        assertThat(movie?.backdrop_path, `is`("/srYya1ZlI97Au4jUYAktDe3avyA.jpg"))
        assertThat(movie?.overview, `is`("Wonder Woman comes into conflict with the Soviet Union during the Cold War in the 1980s and finds a formidable foe by the name of the Cheetah."))
        assertThat(movie?.id, `is`(464052))
    }


    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        val inputStream = javaClass.classLoader!!
            .getResourceAsStream("api-response/$fileName")
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(
            mockResponse
                .setBody(source.readString(Charsets.UTF_8))
        )
    }
}
