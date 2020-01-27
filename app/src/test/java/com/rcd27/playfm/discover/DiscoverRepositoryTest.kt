package com.rcd27.playfm.discover

import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.rcd27.playfm.App
import com.rcd27.playfm.discover.data.DiscoverRepository
import com.rcd27.playfm.discover.data.Recording
import io.reactivex.observers.TestObserver
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DiscoverRepositoryTest {

    private val discoverRepository =
        DiscoverRepository(
            ApplicationProvider.getApplicationContext<App>()
                .applicationComponent
                .discoverApi
        )

    @Test
    fun `Repository returns list of posts`() {
        val testObserver: TestObserver<List<Recording>> = TestObserver.create()

        discoverRepository.getTrendingRecordings()
            .subscribe(testObserver)

        testObserver.await()
        testObserver.assertComplete()
        testObserver.assertNoErrors()

        assertThat(testObserver.values()[0]).isNotEmpty()
    }
}