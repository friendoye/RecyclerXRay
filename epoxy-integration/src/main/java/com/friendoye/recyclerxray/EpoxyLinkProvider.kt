package com.friendoye.recyclerxray

import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyViewHolder
import java.lang.Exception

/**
 * Helper [LoggableLinkProvider] implementation for [Epoxy] library.
 */
class EpoxyLinkProvider : LoggableLinkProvider {
    override fun getLoggableLinkToFileWithClass(
        viewHolder: RecyclerView.ViewHolder,
        clazz: Class<out RecyclerView.ViewHolder>,
    ): String? {
        if (clazz.isAssignableFrom(EpoxyViewHolder::class.java)) {
            viewHolder as EpoxyViewHolder
            viewHolder.model.javaClass.apply {
                try {
                    val constructor = constructors.first()
                    constructor.isAccessible = true
                    val argsTypes = constructor.parameterTypes
                    val array = Array(argsTypes.size) { index -> argsTypes[index].createInstance() }
                    val newInstance = constructor.newInstance(*array) as EpoxyModel<*>
                    val idCounterField = EpoxyModel::class.java.getDeclaredField("addedToAdapter")
                    idCounterField.isAccessible = true
                    idCounterField.set(newInstance, true)
                    newInstance.id(newInstance.id() - 1)
                } catch (e: Exception) {
                    var linkToClass: String?
                    linkToClass =
                        canonicalName?.run {
                            e.stackTrace.findLast { it.toString().contains(this) }?.run {
                                "$fileName:$lineNumber"
                            }
                        }
                    if (linkToClass != null) {
                        return "$simpleName($linkToClass) <-- [Target file is the superclass of given class] "
                    }
                }
            }
        }
        return null
    }

    @Suppress("UNCHECKED_CAST", "IMPLICIT_CAST_TO_ANY")
    private fun <T> Class<T>.createInstance(): T? {
        val value =
            when (this) {
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
