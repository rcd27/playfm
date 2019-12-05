package com.rcd27.playfm.data.post

import com.rcd27.playfm.AppRobolectricTestRunner
import io.reactivex.observers.TestObserver
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AppRobolectricTestRunner::class)
class PostRepositoryTest {

    private val postRepository = PostRepository(
            AppRobolectricTestRunner.pikabuTestcaseApp()
                    .applicationComponent
                    .postApi
    )

    @Test
    fun `Repository returns concrete post`() {
        val testObserver: TestObserver<Post> = TestObserver.create()

        postRepository.getPostById(111) // FIXME: получить существующий айдишник вне теста
                .subscribe(testObserver)

        testObserver.await()
        testObserver.assertComplete()
        testObserver.assertNoErrors()
    }
}