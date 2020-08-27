package com.friendoye.recyclerxray

/**
 * Interface facilitates the easy extraction of custom information
 * about ViewHolder for given position in adapter.
 */
interface XRayCustomParamsAdapterProvider {
    fun provideCustomParams(position: Int): Map<String, Any?>?
}

/**
 * Interface facilitates the easy extraction of custom information
 * about ViewHolder from ViewHolder itself.
 */
interface XRayCustomParamsViewHolderProvider {
    fun provideCustomParams(): Map<String, Any?>?
}
