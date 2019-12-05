package com.rcd27.playfm.dagger.main.post

import com.rcd27.playfm.presentation.post.PostFragment
import dagger.Subcomponent

@Subcomponent(modules = [PostModule::class])
interface PostComponent {

    fun inject(fragment: PostFragment)
}