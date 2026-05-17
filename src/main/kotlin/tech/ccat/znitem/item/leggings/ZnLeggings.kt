package tech.ccat.znitem.item.leggings

import tech.ccat.znitem.item.AbstractZnItem
import tech.ccat.znitem.model.ItemType

abstract class ZnLeggings : AbstractZnItem() {
    override val itemType: ItemType = ItemType.LEGGINGS
    override val reforgeable: Boolean = true
    override val enchantable: Boolean = true
}
