package com.rcd27.playfm

import com.rcd27.playfm.dagger.DaggerApplicationComponent

class UnitTestApp : App() {

    /**
     * Здесь можно подменить реальные даггеровские модули на те, что нам нужны в тестах.
     */
    override fun prepareApplicationComponent(): DaggerApplicationComponent.Builder {
        return super.prepareApplicationComponent()
    }
}