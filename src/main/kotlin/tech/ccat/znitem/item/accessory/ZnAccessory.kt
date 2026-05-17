package tech.ccat.znitem.item.accessory

import tech.ccat.znitem.item.AbstractZnItem
import tech.ccat.znitem.model.ItemType

abstract class ZnAccessory : AbstractZnItem() {
    override val itemType: ItemType = ItemType.ACCESSORY
    override val reforgeable: Boolean = false
    override val enchantable: Boolean = false
}
