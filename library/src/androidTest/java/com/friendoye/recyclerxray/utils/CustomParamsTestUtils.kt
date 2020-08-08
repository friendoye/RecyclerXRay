package com.friendoye.recyclerxray.utils

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.XRayCustomParamsAdapterProvider
import com.friendoye.recyclerxray.XRayCustomParamsViewHolderProvider
import com.friendoye.recyclerxray.XRayDebugViewHolder
import com.friendoye.recyclerxray.XRayResult
import com.friendoye.recyclerxray.test.R


fun createAdapterWithCustomParams(
    viewHoldersParams: List<Map<String, Any?>?>,
    adapterParams: (Int) -> Map<String, Any?>?
): CustomParamsTestAdapter {
    val viewHolderProviders = viewHoldersParams.map {
        { parentView: ViewGroup ->
            val testView = LayoutInflater.from(parentView.context)
                .inflate(R.layout.item_custom_params_test_layout, parentView, false)
            CustomParamsTestViewHolder(testView, createCustomParamDelegate(it))
        }
    }
    return CustomParamsTestAdapter(
        viewHolderProviders, createCustomParamDelegate(adapterParams)
    )
}

private fun createCustomParamDelegate(map: Map<String, Any?>?): XRayCustomParamsViewHolderProvider {
    return object : XRayCustomParamsViewHolderProvider {
        override fun provideCustomParams(): Map<String, Any?>? {
            return map
        }
    }
}

private fun createCustomParamDelegate(mapProvider: (Int) -> Map<String, Any?>?): XRayCustomParamsAdapterProvider {
    return object : XRayCustomParamsAdapterProvider {
        override fun provideCustomParams(position: Int): Map<String, Any?>? {
            return mapProvider(position)
        }
    }
}

class CustomParamsTestAdapter constructor(
    private val viewHolderProviders: List<(ViewGroup) -> RecyclerView.ViewHolder>,
    private val paramsProviderDelegate: XRayCustomParamsAdapterProvider
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    XRayCustomParamsAdapterProvider by paramsProviderDelegate {

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val position = viewType
        return viewHolderProviders[position](parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // Do nothing
    }

    override fun getItemCount(): Int = viewHolderProviders.size
}


class CustomParamsTestViewHolder constructor(
    view: View,
    private val paramsProviderDelegate: XRayCustomParamsViewHolderProvider
) : RecyclerView.ViewHolder(view),
    XRayCustomParamsViewHolderProvider by paramsProviderDelegate


class ResultRecordableXRayDebugViewHolder<K : Any>(
    private val keyProvider: (XRayResult) -> K,
    private val resultMapSizeChanged: (Int, Int) -> Unit = { _, _ -> },
    private val withLogs: Boolean = false
) : XRayDebugViewHolder {
    val resultsMap = mutableMapOf<K, XRayResult>()

    override fun provideView(parent: ViewGroup): View {
        return View(parent.context) //?
    }

    override fun bindView(debugView: View, result: XRayResult) {
        if (withLogs) {
            println("Result for key=${keyProvider(result)}: $result")
        }
        val prevSize = resultsMap.size
        resultsMap[keyProvider(result)] = result
        val currentSize = resultsMap.size
        if (currentSize > prevSize) {
            resultMapSizeChanged(prevSize, currentSize)
        }
    }
}