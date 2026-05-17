package tech.ccat.znitem.item.deployable

import tech.ccat.znitem.item.AbstractZnItem
import tech.ccat.znitem.model.ItemType

abstract class ZnDeployable : AbstractZnItem() {
    override val itemType: ItemType = ItemType.DEPLOYABLE
    override val reforgeable: Boolean = false
    override val enchantable: Boolean = false
}
