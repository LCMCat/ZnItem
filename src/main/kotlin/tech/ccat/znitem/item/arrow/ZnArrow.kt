package tech.ccat.znitem.item.arrow

import tech.ccat.znitem.item.AbstractZnItem
import tech.ccat.znitem.model.ItemType

abstract class ZnArrow : AbstractZnItem() {
    override val itemType: ItemType = ItemType.ARROW
    override val reforgeable: Boolean = false
    override val enchantable: Boolean = true
}
