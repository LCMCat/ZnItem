package tech.ccat.znitem.item.drill

import tech.ccat.znitem.item.AbstractZnItem
import tech.ccat.znitem.model.ItemType

abstract class ZnDrill : AbstractZnItem() {
    override val itemType: ItemType = ItemType.DRILL
    override val reforgeable: Boolean = true
    override val enchantable: Boolean = true
}
