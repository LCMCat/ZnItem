package tech.ccat.znitem.item.memento

import tech.ccat.znitem.item.AbstractZnItem
import tech.ccat.znitem.model.ItemType

abstract class ZnMemento : AbstractZnItem() {
    override val itemType: ItemType = ItemType.MEMENTO
    override val reforgeable: Boolean = false
    override val enchantable: Boolean = false
}
