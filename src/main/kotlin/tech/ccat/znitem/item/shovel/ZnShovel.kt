package tech.ccat.znitem.item.shovel

import tech.ccat.znitem.item.AbstractZnItem
import tech.ccat.znitem.model.ItemType

abstract class ZnShovel : AbstractZnItem() {
    override val itemType: ItemType = ItemType.SHOVEL
    override val reforgeable: Boolean = true
    override val enchantable: Boolean = false
}
