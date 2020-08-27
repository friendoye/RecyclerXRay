package com.friendoye.recyclerxray

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.internal.DelegateRecyclerAdapter
import com.friendoye.recyclerxray.internal.XRayPayload
import com.friendoye.recyclerxray.stubs.TestViewHolder1
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Test

class DelegateRecyclerAdapterTest {

    val adapterMock: RecyclerView.Adapter<RecyclerView.ViewHolder> = mockk(relaxUnitFun = true)
    val parentViewMock: ViewGroup = mockk()
    val recyclerViewMock: RecyclerView = mockk()

    @Test
    fun `Delegate getItemCount()`() {
        every { adapterMock.itemCount } returns 10
        Assert.assertEquals(DelegateRecyclerAdapter(adapterMock).itemCount, 10)
    }

    @Test
    fun `Delegate onCreateViewHolder()`() {
        val viewHolder = TestViewHolder1(parentViewMock)
        every { adapterMock.onCreateViewHolder(parentViewMock, 0) } returns viewHolder
        val result = DelegateRecyclerAdapter(adapterMock).onCreateViewHolder(parentViewMock, 0)
        Assert.assertEquals(result, viewHolder)
    }

    @Test
    fun `Delegate onBindViewHolder()`() {
        val viewHolder = TestViewHolder1(parentViewMock)
        DelegateRecyclerAdapter(adapterMock).onBindViewHolder(viewHolder, 0)
        verify(exactly = 1) { adapterMock.onBindViewHolder(viewHolder, 0) }
        confirmVerified()
    }

    @Test
    fun `Delegate onBindViewHolder() with payload`() {
        val viewHolder = TestViewHolder1(parentViewMock)
        DelegateRecyclerAdapter(adapterMock).onBindViewHolder(viewHolder, 0, listOf(XRayPayload(xRayApiId = 1)))
        verify(exactly = 1) { adapterMock.onBindViewHolder(viewHolder, 0, listOf(XRayPayload(xRayApiId = 1))) }
        confirmVerified()
    }

    @Test
    fun `Delegate getItemViewType()`() {
        every { adapterMock.getItemViewType(5) } returns 2
        val result = DelegateRecyclerAdapter(adapterMock).getItemViewType(5)
        Assert.assertEquals(result, 2)
    }

    @Test
    fun `Delegate onAttachedToRecyclerView()`() {
        DelegateRecyclerAdapter(adapterMock).onAttachedToRecyclerView(recyclerViewMock)
        verify(exactly = 1) { adapterMock.onAttachedToRecyclerView(recyclerViewMock) }
        confirmVerified()
    }

    @Test
    fun `Delegate onDetachedFromRecyclerView()`() {
        DelegateRecyclerAdapter(adapterMock).onDetachedFromRecyclerView(recyclerViewMock)
        verify(exactly = 1) { adapterMock.onDetachedFromRecyclerView(recyclerViewMock) }
        confirmVerified()
    }
}
