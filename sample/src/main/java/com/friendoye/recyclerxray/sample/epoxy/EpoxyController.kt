package com.friendoye.recyclerxray.sample.epoxy

import com.airbnb.epoxy.TypedEpoxyController
import com.friendoye.recyclerxray.sample.ItemType


class EpoxyController : TypedEpoxyController<List<ItemType>>() {
//    @AutoModel var smallModel: SmallModel_? = null
//    @AutoModel var largeModel: LargeModel_? = null

    override fun buildModels(items: List<ItemType>) {
        items.map {
            when (it) {
                is ItemType.Small -> small { 
                    number(it.number).id(it.number)
                        .spanSizeOverride { _, _, _ -> 1 }
                }
                is ItemType.Large -> large {
                    string(it.string).id(it.string)
                        .spanSizeOverride { _, _, _ -> 2 }
                }
                is ItemType.Widest -> widestView {
                    id(it.toString()).spanSizeOverride { _, _, _ -> 3 }
                }
                is ItemType.Empty -> emptyView {
                    id(it.toString()).spanSizeOverride { _, _, _ -> 3 }
                }
                else -> throw IllegalStateException("")
            }
        }
    }
}