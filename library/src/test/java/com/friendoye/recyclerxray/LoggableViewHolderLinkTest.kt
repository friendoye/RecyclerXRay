package com.friendoye.recyclerxray

import com.friendoye.recyclerxray.stubs.CustomArgsViewHolder
import com.friendoye.recyclerxray.stubs.DefaultViewHolder
import com.friendoye.recyclerxray.stubs.PrimitiveArgsViewHolder
import com.friendoye.recyclerxray.stubs.ViewBindingViewHolder
import org.junit.Assert
import org.junit.Test

class LoggableViewHolderLinkTest {

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
}