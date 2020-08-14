package com.friendoye.recyclerxray.sample


sealed class ItemType {
    data class Small(val number: Int) : ItemType()
    data class Large(val string: String) : ItemType()
    object Widest : ItemType()
    object Empty : ItemType()
    data class HorizontalRecycler(val items: List<InnerItemType>) : ItemType()

    companion object {
        fun fromOrdinal(ordinal: Int): Class<out ItemType> {
            return when (ordinal) {
                1 -> Small::class.java
                2 -> Large::class.java
                3 -> Widest::class.java
                4 -> Empty::class.java
                5 -> HorizontalRecycler::class.java
                else -> throw IllegalStateException(
                    "Cannot get ItemType for ordinal = $ordinal"
                )
            }
        }

        inline fun <reified T : ItemType> T.getOrdinal(): Int {
            return when (this) {
                is Small -> 1
                is Large -> 2
                is Widest -> 3
                is Empty -> 4
                is HorizontalRecycler -> 5
                else -> throw IllegalStateException("Should not happen!")
            }
        }
    }
}