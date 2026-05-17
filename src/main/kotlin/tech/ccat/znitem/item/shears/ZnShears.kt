package tech.ccat.znitem.item.shears

import tech.ccat.znitem.item.AbstractZnItem
import tech.ccat.znitem.model.ItemType

abstract class ZnShears : AbstractZnItem() {
    override val itemType: ItemType = ItemType.SHEARS
    override val reforgeable: Boolean = true
    override val enchantable: Boolean = false
}
