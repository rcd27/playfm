package com.rcd27.playfm.navigation

/** Алгебраический тип для описания навигации */
sealed class AppDestination

/**  Экран постов. */
object FeedScreen : AppDestination()

// TODO: добавить и захэндлить навигацию до поста