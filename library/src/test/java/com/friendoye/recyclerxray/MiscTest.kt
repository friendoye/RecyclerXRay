package com.friendoye.recyclerxray

import androidx.recyclerview.widget.ConcatAdapter
import com.friendoye.recyclerxray.internal.RealRecyclerXRayApi
import com.friendoye.recyclerxray.internal.checkIsWrappedCorrectly
import com.friendoye.recyclerxray.stubs.StubRecyclerAdapter
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MiscTest {

    lateinit var localRecyclerXRay: LocalRecyclerXRay

    @Before
    fun setup() {
        localRecyclerXRay = LocalRecyclerXRay {
            RealRecyclerXRayApi()
        }
    }

    @Test
    fun `checkIsWrappedCorrectly() for regular Adapter returns false`() {
        val adapter = StubRecyclerAdapter()

        Assert.assertFalse(adapter.checkIsWrappedCorrectly())
    }

    @Test
    fun `checkIsWrappedCorrectly() for wrapped regular Adapter returns false`() {
        val adapter = localRecyclerXRay.wrap(StubRecyclerAdapter())

        Assert.assertTrue(adapter.checkIsWrappedCorrectly())
    }

    @Test
    fun `checkIsWrappedCorrectly() for ConcatAdapter with common viewTypes returns false`() {
        val adapter = ConcatAdapter(
            ConcatAdapter.Config.Builder()
                .setIsolateViewTypes(false)
                .build(),
            listOf(
                StubRecyclerAdapter()
            )
        )

        Assert.assertFalse(adapter.checkIsWrappedCorrectly())
    }

    @Test
    fun `checkIsWrappedCorrectly() for wrapped ConcatAdapter with common viewTypes returns true`() {
        val adapter = localRecyclerXRay.wrap(
            ConcatAdapter(
                ConcatAdapter.Config.Builder()
                    .setIsolateViewTypes(false)
                    .build(),
                listOf(
                    StubRecyclerAdapter()
                )
            )
        )

        Assert.assertTrue(adapter.checkIsWrappedCorrectly())
    }

    @Test
    fun `checkIsWrappedCorrectly() for ConcatAdapter with partly wrapped inner adapters and with common viewTypes returns false`() {
        val adapter = ConcatAdapter(
            ConcatAdapter.Config.Builder()
                .setIsolateViewTypes(false)
                .build(),
            listOf(
                StubRecyclerAdapter(),
                localRecyclerXRay.wrap(StubRecyclerAdapter())
            )
        )

        Assert.assertFalse(adapter.checkIsWrappedCorrectly())
    }

    @Test
    fun `checkIsWrappedCorrectly() for ConcatAdapter with all wrapped inner adapters with common viewTypes returns true`() {
        val adapter = ConcatAdapter(
            ConcatAdapter.Config.Builder()
                .setIsolateViewTypes(false)
                .build(),
            listOf(
                localRecyclerXRay.wrap(StubRecyclerAdapter()),
                localRecyclerXRay.wrap(StubRecyclerAdapter())
            )
        )

        Assert.assertTrue(adapter.checkIsWrappedCorrectly())
    }

    @Test
    fun `checkIsWrappedCorrectly() for ConcatAdapter with with isolated viewTypes returns true`() {
        val adapter = ConcatAdapter(
            ConcatAdapter.Config.Builder()
                .setIsolateViewTypes(true)
                .build(),
            listOf(
                StubRecyclerAdapter(),
                localRecyclerXRay.wrap(StubRecyclerAdapter())
            )
        )

        Assert.assertTrue(adapter.checkIsWrappedCorrectly())
    }
}
