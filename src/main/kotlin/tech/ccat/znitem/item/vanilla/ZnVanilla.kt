package tech.ccat.znitem.item.vanilla

import tech.ccat.znitem.item.AbstractZnItem
import tech.ccat.znitem.model.ItemType

abstract class ZnVanilla : AbstractZnItem() {
    override val itemType: ItemType = ItemType.VANILLA
    override val reforgeable: Boolean = false
    override val enchantable: Boolean = false
}
