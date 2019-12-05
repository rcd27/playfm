package com.rcd27.playfm.dagger

import android.content.Context
import com.rcd27.playfm.BuildConfig
import com.rcd27.playfm.api.discover.DiscoverApi
import com.rcd27.playfm.api.post.PostApi
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@Module
class ApplicationModule @Inject constructor(private val appContext: Context) {

    // FIXME: don't pass AppContext where ever you want
    @Provides
    fun appContext(): Context = appContext

    @Provides
    fun feedApi(retrofit: Retrofit): DiscoverApi {
        return retrofit.create(DiscoverApi::class.java)
    }

    @Provides
    fun postApi(retrofit: Retrofit): PostApi {
        return retrofit.create(PostApi::class.java)
    }

    @Provides
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
    fun retrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }
}