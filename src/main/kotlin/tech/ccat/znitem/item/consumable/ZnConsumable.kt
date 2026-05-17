package tech.ccat.znitem.item.consumable

import tech.ccat.znitem.item.AbstractZnItem
import tech.ccat.znitem.model.ItemType

abstract class ZnConsumable : AbstractZnItem() {
    override val itemType: ItemType = ItemType.CONSUMABLE
    override val reforgeable: Boolean = false
    override val enchantable: Boolean = false
}
