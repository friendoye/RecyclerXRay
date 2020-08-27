package com.friendoye.recyclerxray.internal

import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.LoggableLinkProvider

/**
 * [LoggableLinkProvider] implementation, that returns first `non-null` link from
 * provided [LoggableLinkProvider]s, otherwise - `null`.
 */
internal class CompositeLoggableLinkProvider(
    private val linkProviders: List<LoggableLinkProvider>
) : LoggableLinkProvider {
    override fun getLoggableLinkToFileWithClass(clazz: Class<out RecyclerView.ViewHolder>): String? {
        return linkProviders.asSequence()
            .map { it.getLoggableLinkToFileWithClass(clazz) }
            .filterNotNull()
            .firstOrNull()
    }
}

/**
 * Default [LoggableLinkProvider] implementation, that works for any [RecyclerView.ViewHolder].
 */
internal class DefaultLoggableLinkProvider : LoggableLinkProvider {
    override fun getLoggableLinkToFileWithClass(clazz: Class<out RecyclerView.ViewHolder>): String? {
        clazz.apply {
            try {
                val constructor = constructors.first()
                constructor.isAccessible = true
                val argsTypes = constructor.parameterTypes
                val array = Array(argsTypes.size) { index -> argsTypes[index].createInstance() }
                constructor.newInstance(*array)
            } catch (e: Exception) {
                var linkToClass: String?
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
        }
        return null
    }

    @Suppress("UNCHECKED_CAST", "IMPLICIT_CAST_TO_ANY")
    private fun <T> Class<T>.createInstance(): T? {
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
}
