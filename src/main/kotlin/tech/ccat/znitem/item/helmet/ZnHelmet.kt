package tech.ccat.znitem.item.helmet

import tech.ccat.znitem.item.AbstractZnItem
import tech.ccat.znitem.model.ItemType

abstract class ZnHelmet : AbstractZnItem() {
    override val itemType: ItemType = ItemType.HELMET
    override val reforgeable: Boolean = true
    override val enchantable: Boolean = true
}
