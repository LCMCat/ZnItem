package tech.ccat.znitem.item.bow

import tech.ccat.znitem.item.AbstractZnItem
import tech.ccat.znitem.model.ItemType

abstract class ZnBow : AbstractZnItem() {
    override val itemType: ItemType = ItemType.BOW
    override val reforgeable: Boolean = true
    override val enchantable: Boolean = true
}
