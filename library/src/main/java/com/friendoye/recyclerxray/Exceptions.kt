package com.friendoye.recyclerxray

/**
 * Exception to indicate, that [LocalRecyclerXRay] were used before proper initialization.
 */
class RecyclerXRayIsNotInitializedException : IllegalStateException(
    "RecyclerXRay wasn't initialized. Use one of RecyclerXRay.init() methods."
)

/**
 * Exception to indicate, that several [LocalRecyclerXRay] were used to wrap same [RecyclerView.Adapter].
 */
class MultipleRecyclerXRayAttachedException(message: String) : IllegalStateException(message)