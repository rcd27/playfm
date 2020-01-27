package com.rcd27.playfm

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

open class App : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        /** Time API for JSR-310 (see https://jcp.org/en/jsr/detail?id=310) */
        AndroidThreeTen.init(this)

        applicationComponent = prepareApplicationComponent().build()
    }

    /**
     * Для переопределения в тестовой среде
     */
    protected open fun prepareApplicationComponent(): DaggerApplicationComponent.Builder {
        return DaggerApplicationComponent.builder()
            .applicationModule(
                ApplicationModule(
                    applicationContext
                )
            )
    }
}