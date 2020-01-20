package com.rcd27.playfm.domain.auth

import com.jakewharton.rxrelay2.BehaviorRelay

class AuthStateMachine {

    val state: BehaviorRelay<AuthState> = BehaviorRelay.create()

}

sealed class AuthState
object LogIned : AuthState()
object NotLogIned : AuthState()