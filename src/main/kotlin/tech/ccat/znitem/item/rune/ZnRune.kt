package tech.ccat.znitem.item.rune

import tech.ccat.znitem.item.AbstractZnItem
import tech.ccat.znitem.model.ItemType

abstract class ZnRune : AbstractZnItem() {
    override val itemType: ItemType = ItemType.RUNE
    override val reforgeable: Boolean = false
    override val enchantable: Boolean = false
}
