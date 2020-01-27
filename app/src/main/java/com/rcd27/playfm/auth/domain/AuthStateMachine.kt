package com.rcd27.playfm.auth.domain

import com.jakewharton.rxrelay2.BehaviorRelay
import javax.inject.Inject

class AuthStateMachine @Inject constructor() {

    // FIXME: this create default logic should change since we save Login state locally
    val state: BehaviorRelay<AuthState> = BehaviorRelay.createDefault(NotLogined)
}

sealed class AuthState
object Logined : AuthState()
object NotLogined : AuthState()