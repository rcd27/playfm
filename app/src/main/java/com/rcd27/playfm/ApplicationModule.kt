package com.rcd27.playfm

import android.content.Context
import com.rcd27.playfm.auth.domain.AuthStateMachine
import com.rcd27.playfm.discover.api.DiscoverApi
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Module
class ApplicationModule @Inject constructor(private val appContext: Context) {

    // FIXME: don't pass AppContext where ever you want
    @Provides
    fun appContext(): Context = appContext

    @Provides
    @Singleton
    fun feedApi(retrofit: Retrofit): DiscoverApi {
        return retrofit.create(DiscoverApi::class.java)
    }

    @Provides
    @Singleton
    fun okhttpClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            // FIXME: we need to provide HttpLogging interceptors for unit tests and apk.
            // Unit tests should print in console, apk uses Log.X
            .addInterceptor(HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    println(message)
                }
            }).apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .build()
    }

    @Provides
    @Singleton
    fun retrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }

    @Provides
    @Singleton
    fun authStateMachine(): AuthStateMachine =
        AuthStateMachine()
}