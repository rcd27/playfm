package com.rcd27.playfm.data.discover

import com.google.common.truth.Truth.assertThat
import com.rcd27.playfm.AppRobolectricTestRunner
import io.reactivex.observers.TestObserver
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AppRobolectricTestRunner::class)
class DiscoverRepositoryTest {

    private val discoverRepository = DiscoverRepository(
            AppRobolectricTestRunner
                    .pikabuTestcaseApp()
                    .applicationComponent
                    .discoverApi
    )

    @Test
    fun `Repository returns list of posts`() {
        val testObserver: TestObserver<List<DiscoverItem>> = TestObserver.create()

        discoverRepository.getTrendingRecordings()
                .subscribe(testObserver)

        testObserver.await()
        testObserver.assertComplete()
        testObserver.assertNoErrors()

        assertThat(testObserver.values()[0]).isNotEmpty()
    }
}