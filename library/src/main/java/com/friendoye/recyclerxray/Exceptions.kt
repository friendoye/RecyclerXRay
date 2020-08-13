package com.friendoye.recyclerxray

class RecyclerXRayIsNotInitializedException : IllegalStateException(
    "RecyclerXRay wasn't initialized. Use one of RecyclerXRay.init() methods."
)

class MultipleRecyclerXRayAttachedException(message: String) : IllegalStateException(message)