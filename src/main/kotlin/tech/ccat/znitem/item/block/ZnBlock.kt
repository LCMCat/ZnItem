package tech.ccat.znitem.item.block

import tech.ccat.znitem.item.AbstractZnItem
import tech.ccat.znitem.model.ItemType

abstract class ZnBlock : AbstractZnItem() {
    override val itemType: ItemType = ItemType.BLOCK
    override val reforgeable: Boolean = false
    override val enchantable: Boolean = false
}
