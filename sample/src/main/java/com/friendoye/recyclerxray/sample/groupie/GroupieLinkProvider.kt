package com.friendoye.recyclerxray.sample.groupie

import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.LoggableLinkProvider
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import java.lang.Exception

class GroupieLinkProvider : LoggableLinkProvider {

    override fun getLoggableLinkToFileWithClass(viewHolder: RecyclerView.ViewHolder,
                                                clazz: Class<out RecyclerView.ViewHolder>): String? {
        if (clazz.isAssignableFrom(GroupieViewHolder::class.java)) {
            viewHolder as GroupieViewHolder
            viewHolder.item.javaClass.apply {
                val idCounterField = Item::class.java.getDeclaredField("ID_COUNTER")
                idCounterField.isAccessible = true
                val prevValue = idCounterField.get(viewHolder.item)
                idCounterField.set(viewHolder.item, null)
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
                    if (linkToClass != null) {
                        return "$simpleName($linkToClass)"
                    }
                }
                idCounterField.set(viewHolder.item, prevValue)
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
