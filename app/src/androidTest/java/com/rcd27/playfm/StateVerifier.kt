package com.rcd27.playfm

import com.google.common.truth.Truth
import com.jakewharton.rxrelay2.ReplayRelay
import java.util.concurrent.TimeUnit

class StateVerifier<in S>(private val stateObservable: ReplayRelay<S>) {
    private var alreadyVerifiedStates: List<S> = emptyList()

    @Synchronized
    fun assertNextState(nextExpectedState: S) {

        val expectedStates: List<S> = alreadyVerifiedStates + nextExpectedState
        val actualStates: List<S> = stateObservable.take(alreadyVerifiedStates.size + 1L)
            .timeout(5, TimeUnit.SECONDS)
            .toList()
            .blockingGet()

        Truth.assertThat(actualStates).containsExactlyElementsIn(expectedStates).inOrder()

        alreadyVerifiedStates = actualStates
    }
}