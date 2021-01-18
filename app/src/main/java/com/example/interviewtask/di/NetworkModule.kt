package com.example.interviewtask.di

import android.content.Context
import com.example.interviewtask.BuildConfig
import com.example.interviewtask.data.remote.TmdbService
import com.example.interviewtask.utils.Constants.API_KEY_HEADER
import com.example.interviewtask.utils.Constants.BASE_URL
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.*
import retrofit2.Retrofit
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkhttpClient(@ApplicationContext context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(Cache(File(context.cacheDir, "api_cache"), 50 * 1024 * 1024))
            .cache(null)
            .connectionPool(ConnectionPool(10, 2, TimeUnit.MINUTES))
            .dispatcher(
                Dispatcher().apply {
                    maxRequestsPerHost = 15
                }
            )
            .addInterceptor(provideInterceptor())
            .build()
    }

    @ExperimentalSerializationApi
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(Json {
                ignoreUnknownKeys = true
            }.asConverterFactory(MediaType.get("application/json")))
            .build()
    }


    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): TmdbService {
        return retrofit.create(TmdbService::class.java)
    }

    @Provides
    fun provideInterceptor(): Interceptor {
        return Interceptor {
            val request: Request = it.request()
            val url = request.url()
                .newBuilder()
                .addQueryParameter(API_KEY_HEADER, BuildConfig.API_KEY)
                .build()
            it.proceed(request.newBuilder().url(url).build())
        }
    }
}