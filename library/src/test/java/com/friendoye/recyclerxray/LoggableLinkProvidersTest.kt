package com.friendoye.recyclerxray

import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.internal.CompositeLoggableLinkProvider
import com.friendoye.recyclerxray.internal.DefaultLoggableLinkProvider
import com.friendoye.recyclerxray.stubs.CustomArgsViewHolder
import com.friendoye.recyclerxray.stubs.DefaultViewHolder
import com.friendoye.recyclerxray.stubs.PrimitiveArgsViewHolder
import com.friendoye.recyclerxray.stubs.ViewBindingViewHolder
import org.junit.Assert
import org.junit.Test

class DefaultLoggableLinkProviderTest {

    @Test
    fun `Create loggable link for ViewHolder with default constructor`() {
        val link = DefaultViewHolder::class.java.getLoggableLinkToFileWithClass()
        Assert.assertEquals(link, "DefaultViewHolder(LoggableStubViewHolders.kt:7)")
    }

    @Test
    fun `Create loggable link for ViewHolder with ViewBinding arg`() {
        val link = ViewBindingViewHolder::class.java.getLoggableLinkToFileWithClass()
        Assert.assertEquals(link, "ViewBindingViewHolder(LoggableStubViewHolders.kt:11)")
    }

    @Test
    fun `Create loggable link for ViewHolder with primitive args`() {
        val link = PrimitiveArgsViewHolder::class.java.getLoggableLinkToFileWithClass()
        Assert.assertEquals(link, "PrimitiveArgsViewHolder(LoggableStubViewHolders.kt:22)")
    }

    @Test
    fun `Create loggable link for ViewHolder with custom args`() {
        val link = CustomArgsViewHolder::class.java.getLoggableLinkToFileWithClass()
        Assert.assertEquals(link, "CustomArgsViewHolder(LoggableStubViewHolders.kt:28)")
    }

    private fun Class<out RecyclerView.ViewHolder>.getLoggableLinkToFileWithClass(): String? {
        val linkProvider = DefaultLoggableLinkProvider()
        return linkProvider.getLoggableLinkToFileWithClass(this)
    }
}

class CompositeLoggableLinkProviderTest {

    @Test
    fun `Return first non-null link`() {
        val linkProvider = CompositeLoggableLinkProvider(listOf(
            { _: Class<*> -> null }.asLinkProvider(),
            { _: Class<*> -> "I love links way to much!" }.asLinkProvider(),
            { _: Class<*> -> null }.asLinkProvider()
        ))
        val link = linkProvider.getLoggableLinkToFileWithClass(DefaultViewHolder::class.java)
        Assert.assertEquals(link, "I love links way to much!")
    }

    @Test
    fun `Return null if all providers returned null`() {
        val linkProvider = CompositeLoggableLinkProvider(listOf(
            { _: Class<*> -> null }.asLinkProvider(),
            { _: Class<*> -> null }.asLinkProvider()
        ))
        val link = linkProvider.getLoggableLinkToFileWithClass(DefaultViewHolder::class.java)
        Assert.assertNull(link)
    }

    private fun ((Class<*>) -> String?).asLinkProvider(): LoggableLinkProvider {
        return object : LoggableLinkProvider {
            override fun getLoggableLinkToFileWithClass(clazz: Class<out RecyclerView.ViewHolder>): String? {
                return this@asLinkProvider.invoke(clazz)
            }
        }
    }
}
