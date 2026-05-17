package tech.ccat.znitem.item.other

import tech.ccat.znitem.item.AbstractZnItem
import tech.ccat.znitem.model.ItemType

abstract class ZnOther : AbstractZnItem() {
    override val itemType: ItemType = ItemType.OTHER
    override val reforgeable: Boolean = false
    override val enchantable: Boolean = false
}
