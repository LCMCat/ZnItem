package tech.ccat.znitem.item.axe

import tech.ccat.znitem.item.AbstractZnItem
import tech.ccat.znitem.model.ItemType

abstract class ZnAxe : AbstractZnItem() {
    override val itemType: ItemType = ItemType.AXE
    override val reforgeable: Boolean = true
    override val enchantable: Boolean = true
}
