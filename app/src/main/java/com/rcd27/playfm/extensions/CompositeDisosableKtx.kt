package com.rcd27.playfm.extensions

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/** Утилита, добавляющая возможность складывать [Disposable] в [CompositeDisposable]
 * через  оператор присвоения `+=`
 */
operator fun CompositeDisposable.plusAssign(d: Disposable) {
    this.add(d)
}