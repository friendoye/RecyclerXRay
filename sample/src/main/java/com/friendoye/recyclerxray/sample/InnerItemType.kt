package com.friendoye.recyclerxray.sample

sealed class InnerItemType {
    object Box : InnerItemType()
    object Circle : InnerItemType()

    companion object {
        fun fromOrdinal(ordinal: Int): Class<out InnerItemType> {
            return when (ordinal) {
                1 -> Box::class.java
                2 -> Circle::class.java
                else -> throw IllegalStateException(
                    "Cannot get InnerItemType for ordinal = $ordinal"
                )
            }
        }

        inline fun <reified T : InnerItemType> T.getOrdinal(): Int {
            return when (this) {
                is Box -> 1
                is Circle -> 2
                else -> throw IllegalStateException("Should not happen!")
            }
        }
    }
}
