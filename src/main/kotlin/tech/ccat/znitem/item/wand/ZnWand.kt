package tech.ccat.znitem.item.wand

import tech.ccat.znitem.item.AbstractZnItem
import tech.ccat.znitem.model.ItemType

abstract class ZnWand : AbstractZnItem() {
    override val itemType: ItemType = ItemType.WAND
    override val reforgeable: Boolean = true
    override val enchantable: Boolean = true
}
