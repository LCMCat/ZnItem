package tech.ccat.znitem.item.pet

import tech.ccat.znitem.item.AbstractZnItem
import tech.ccat.znitem.model.ItemType

abstract class ZnPet : AbstractZnItem() {
    override val itemType: ItemType = ItemType.PET
    override val reforgeable: Boolean = false
    override val enchantable: Boolean = false
}
