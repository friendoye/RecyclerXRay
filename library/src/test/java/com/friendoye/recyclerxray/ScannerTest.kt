package com.friendoye.recyclerxray

import android.view.View
import com.friendoye.recyclerxray.internal.Scanner
import com.friendoye.recyclerxray.stubs.TestViewHolder1
import com.friendoye.recyclerxray.stubs.TestViewHolder2
import com.friendoye.recyclerxray.stubs.TestViewHolder3
import com.friendoye.recyclerxray.stubs.TestViewHolder4
import com.friendoye.recyclerxray.stubs.XRayCustomParamsDelegateViewHolder
import io.mockk.mockk
import java.util.Random
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ScannerTest {

    val mockView = mockk<View>(relaxed = true)

    @Test
    fun `Different colorRandoms produce different color sequences`() {
        val scanners = listOf(Scanner(Random(1000L)), Scanner(Random(2000L)))
        val viewHolders = listOf(::TestViewHolder1, ::TestViewHolder2, ::TestViewHolder3, ::TestViewHolder4)
            .map { it(mockView) }

        val colorSequences = scanners.map { scanner ->
            viewHolders
                .map { scanner.scan(it, 1, null, mockView).color }
        }

        Assert.assertNotEquals(colorSequences[0], colorSequences[1])
    }

    @Test
    fun `ViewHolders with same item type and same class have same color`() {
        val scanner = Scanner()

        val firstResult = scanner.scan(TestViewHolder1(mockView), 1, null, mockView)
        val secondResult = scanner.scan(TestViewHolder1(mockView), 1, null, mockView)

        Assert.assertEquals(firstResult.color, secondResult.color)
    }

    @Test
    fun `ViewHolders with different item type have different color`() {
        val scanner = Scanner()

        val firstResult = scanner.scan(TestViewHolder1(mockView), 1, null, mockView)
        val secondResult = scanner.scan(TestViewHolder1(mockView), 2, null, mockView)

        Assert.assertNotEquals(firstResult.color, secondResult.color)
    }

    @Test
    fun `ViewHolders with different class have different color`() {
        val scanner = Scanner()

        val firstResult = scanner.scan(TestViewHolder1(mockView), 1, null, mockView)
        val secondResult = scanner.scan(TestViewHolder2(mockView), 1, null, mockView)

        Assert.assertNotEquals(firstResult.color, secondResult.color)
    }

    @Test
    fun `Custom params are correctly merged`() {
        val scanner = Scanner()

        val customParamsViewHolder = XRayCustomParamsDelegateViewHolder(
            paramsProviderDelegate = object : XRayCustomParamsViewHolderProvider {
                override fun provideCustomParams(): Map<String, Any?>? {
                    return mapOf(
                        "delegate_param_1" to 12,
                        "delegate_param_2" to "inner_string",
                        "overridden_param" to "delegate_param"
                    )
                }
            },
            view = mockView
        )

        val result = scanner.scan(
            customParamsViewHolder,
            1,
            mapOf(
                "provided_param_1" to 10,
                "provided_param_2" to "outer_string",
                "overridden_param" to "provided_param"
            ),
            mockView
        )

        Assert.assertEquals(result.customParams, mapOf(
            "delegate_param_1" to 12,
            "delegate_param_2" to "inner_string",
            "provided_param_1" to 10,
            "provided_param_2" to "outer_string",
            "overridden_param" to "provided_param"
        ))
    }
}
