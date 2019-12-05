package com.rcd27.playfm.extensions

/** Для использования с ключевым `when`. При добавлении, компилятор требует, чтобы
 * были обработаны все кейсы.
 * see: https://proandroiddev.com/til-when-is-when-exhaustive-31d69f630a8b
 */
val <T> T.exhaustive: T
    get() = this