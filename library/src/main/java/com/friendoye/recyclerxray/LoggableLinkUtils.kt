package com.friendoye.recyclerxray

import androidx.recyclerview.widget.RecyclerView

fun Class<out RecyclerView.ViewHolder>.getLoggableLinkToFileWithClass(): String? {
    try {
        val constructor = constructors.first()
        constructor.isAccessible = true
        val argsTypes = constructor.parameterTypes
        val array = Array(argsTypes.size) { index -> argsTypes[index].createInstance() }
        constructor.newInstance(*array)
    } catch (e: Exception) {
        var linkToClass: String?
        // TODO: Think about Kotlin non-null check: Intrinsics.checkParameterIsNotNull
        linkToClass = canonicalName?.run {
            e.cause?.stackTrace?.find { it.toString().contains(this) }?.run {
                "$fileName:$lineNumber"
            }
        }
        if (linkToClass == null) {
            linkToClass = e.cause?.stackTrace?.get(0)?.run {
                "$fileName:$lineNumber"
            }
        }
        if (linkToClass == null || linkToClass.startsWith("RecyclerView.java")) {
            linkToClass = e.cause?.stackTrace?.get(1)?.run {
                "$fileName:$lineNumber"
            }
        }
        return "$simpleName($linkToClass)"
    }
    return null
}

@Suppress("UNCHECKED_CAST")
internal fun <T> Class<T>.createInstance(): T? {
    val value = when (this) {
        Byte::class.java -> 0.toByte()
        Short::class.java -> 0.toShort()
        Int::class.java -> 0
        Long::class.java -> 0L
        Float::class.java -> 0f
        Double::class.java -> 0.0
        Char::class.java -> ' '
        String::class.java -> ""
        Boolean::class.java -> false
        else -> null
    }
    return value as T?
}