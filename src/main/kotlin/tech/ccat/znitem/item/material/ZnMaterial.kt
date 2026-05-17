package tech.ccat.znitem.item.material

import tech.ccat.znitem.item.AbstractZnItem
import tech.ccat.znitem.model.ItemType

abstract class ZnMaterial : AbstractZnItem() {
    override val itemType: ItemType = ItemType.MATERIAL
    override val reforgeable: Boolean = false
    override val enchantable: Boolean = false
}
