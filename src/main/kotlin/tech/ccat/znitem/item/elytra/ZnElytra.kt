package tech.ccat.znitem.item.elytra

import tech.ccat.znitem.item.AbstractZnItem
import tech.ccat.znitem.model.ItemType

abstract class ZnElytra : AbstractZnItem() {
    override val itemType: ItemType = ItemType.ELYTRA
    override val reforgeable: Boolean = true
    override val enchantable: Boolean = false
}
