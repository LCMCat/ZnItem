package tech.ccat.znitem.item.chestplate

import tech.ccat.znitem.item.AbstractZnItem
import tech.ccat.znitem.model.ItemType

abstract class ZnChestplate : AbstractZnItem() {
    override val itemType: ItemType = ItemType.CHESTPLATE
    override val reforgeable: Boolean = true
    override val enchantable: Boolean = true
}
