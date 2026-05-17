package tech.ccat.znitem.item.pickaxe

import tech.ccat.znitem.item.AbstractZnItem
import tech.ccat.znitem.model.ItemType

abstract class ZnPickaxe : AbstractZnItem() {
    override val itemType: ItemType = ItemType.PICKAXE
    override val reforgeable: Boolean = true
    override val enchantable: Boolean = false
}
