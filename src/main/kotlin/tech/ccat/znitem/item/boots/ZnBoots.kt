package tech.ccat.znitem.item.boots

import tech.ccat.znitem.item.AbstractZnItem
import tech.ccat.znitem.model.ItemType

abstract class ZnBoots : AbstractZnItem() {
    override val itemType: ItemType = ItemType.BOOTS
    override val reforgeable: Boolean = true
    override val enchantable: Boolean = true
}
