package tech.ccat.znitem.item.hoe

import tech.ccat.znitem.item.AbstractZnItem
import tech.ccat.znitem.model.ItemType

abstract class ZnHoe : AbstractZnItem() {
    override val itemType: ItemType = ItemType.HOE
    override val reforgeable: Boolean = true
    override val enchantable: Boolean = false
}
