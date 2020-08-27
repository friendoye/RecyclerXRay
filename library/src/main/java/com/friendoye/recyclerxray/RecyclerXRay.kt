package com.friendoye.recyclerxray

/**
 * Global instance of [LocalRecyclerXRay]. Created for convenience.
 *
 * Example of usage:
 *
 *     val wrappedAdapter = RecyclerXRay.wrap(adapter)
 *     recyclerView.adapter = wrappedAdapter
 *
 *     RecyclerXRay.showSecrets()
 *
 * WARNING: Do not use [RecyclerXRay] in tests, because we cannot reset its state!
 */
object RecyclerXRay : LocalRecyclerXRay()
