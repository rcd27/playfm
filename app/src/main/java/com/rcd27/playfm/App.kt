package com.rcd27.playfm

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.rcd27.playfm.dagger.ApplicationComponent
import com.rcd27.playfm.dagger.ApplicationModule
import com.rcd27.playfm.dagger.DaggerApplicationComponent

open class App : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        /** [org.threeten.bp]*/
        AndroidThreeTen.init(this)

        applicationComponent = prepareApplicationComponent().build()
    }

    /**
     * Для переопределения в тестовой среде
     */
    protected open fun prepareApplicationComponent(): DaggerApplicationComponent.Builder {
        return DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(applicationContext))
    }
}