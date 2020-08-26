package com.friendoye.recyclerxray

import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.friendoye.recyclerxray.utils.ResultRecordableXRayDebugViewHolder
import com.friendoye.recyclerxray.utils.TestActivity
import com.friendoye.recyclerxray.utils.createAdapterWithCustomParams
import java.util.concurrent.CountDownLatch
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class XRayCustomParamsProvidersTest {

    @Before
    fun setup() {
        XRayInitializer.init(isNoOpMode = false)
    }

    @After
    fun teardown() {
        XRayInitializer.reset()
    }

    @Test
    fun customParams() {
        val viewHoldersParams = listOf(
            mapOf("param" to "test1"),
            mapOf("param" to 2),
            mapOf("param" to "test3", "another_param" to 3),
            null,
            null,
            mapOf("another_param" to 6),
            mapOf("another_param" to "test7"),
            null,
            null,
            mapOf("another_param" to 10),
            mapOf("another_param" to "test11")
        )
        val adapterParams = { position: Int -> mapOf("adapter_param" to position + 1) }

        val testAdapter = createAdapterWithCustomParams(viewHoldersParams, adapterParams)
        val latch = CountDownLatch(testAdapter.itemCount)
        val testDebugViewHolder = ResultRecordableXRayDebugViewHolder(
            keyProvider = { result -> result.viewHolderType },
            resultMapSizeChanged = { prevSize, currentSize ->
                repeat(currentSize - prevSize) {
                    latch.countDown()
                }
            }
        )

        val recyclerXRay = LocalRecyclerXRay()
        recyclerXRay.settings = XRaySettings.Builder()
            .apply {
                debugViewHolder = testDebugViewHolder
            }
            .build()

        val scenario = launchActivity<TestActivity>()
        scenario.onActivity { activity ->
            activity.testRecycler.adapter = recyclerXRay.wrap(testAdapter)
            recyclerXRay.showSecrets()
        }

        // Awaiting receiving all XRayResult via RV binding
        latch.await()

        Assert.assertEquals(
            listOf(
                mapOf("param" to "test1", "adapter_param" to 1),
                mapOf("param" to 2, "adapter_param" to 2),
                mapOf("param" to "test3", "another_param" to 3, "adapter_param" to 3),
                mapOf("adapter_param" to 4),
                mapOf("adapter_param" to 5),
                mapOf("another_param" to 6, "adapter_param" to 6),
                mapOf("another_param" to "test7", "adapter_param" to 7),
                mapOf("adapter_param" to 8),
                mapOf("adapter_param" to 9),
                mapOf("another_param" to 10, "adapter_param" to 10),
                mapOf("another_param" to "test11", "adapter_param" to 11)
            ),
            testDebugViewHolder.resultsMap
                .toList()
                .sortedBy { it.first }
                .map { it.second.customParams }
        )
    }
}
