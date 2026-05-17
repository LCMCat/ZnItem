package tech.ccat.znitem.item.fishingrod

import tech.ccat.znitem.item.AbstractZnItem
import tech.ccat.znitem.model.ItemType

abstract class ZnFishingRod : AbstractZnItem() {
    override val itemType: ItemType = ItemType.FISHING_ROD
    override val reforgeable: Boolean = true
    override val enchantable: Boolean = true
}
