package com.friendoye.recyclerxray

import android.graphics.Color
import com.friendoye.recyclerxray.internal.generateColorSequence
import com.friendoye.recyclerxray.internal.provideDefaultColorGeneratorRandom
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ColorGenerationTest {

    @Test
    fun `5 generated colors have evenly distributed hue`() {
        `test n generated colors have evenly distributed hue`(colorsAmount = 5)
    }

    @Test
    fun `10 generated colors have evenly distributed hue`() {
        `test n generated colors have evenly distributed hue`(colorsAmount = 10)
    }

    @Test
    fun `20 generated colors have evenly distributed hue`() {
        `test n generated colors have evenly distributed hue`(colorsAmount = 20)
    }

    @Test
    fun `50 generated colors have evenly distributed hue`() {
        `test n generated colors have evenly distributed hue`(colorsAmount = 50)
    }

    @Test
    fun `100 generated colors have evenly distributed hue`() {
        `test n generated colors have evenly distributed hue`(colorsAmount = 100)
    }

    private fun `test n generated colors have evenly distributed hue`(colorsAmount: Int) {
        val averageHueDistance = 360.0f / colorsAmount

        val random = provideDefaultColorGeneratorRandom()
        val hsvArray = FloatArray(3)
        val generatedHues = random.generateColorSequence().take(colorsAmount)
            .map {
                Color.colorToHSV(it, hsvArray)
                hsvArray[0] // Extract HUE component
            }
            .sorted().toMutableList()
        val closestHueDistance = generatedHues
            // Add first element to the end, so we can compute
            // HUE distance between extremes.
            .apply { add(360.0f + first()) }
            .zipWithNext { hue1, hue2 -> hue2 - hue1 }
            .min() ?: throw IllegalStateException("Couldn't find min hue distance!")

        println("Avg hue distance: $averageHueDistance")
        println("Min closest hue distance: $closestHueDistance")

        Assert.assertTrue(averageHueDistance / 3 <= closestHueDistance)
    }
}
