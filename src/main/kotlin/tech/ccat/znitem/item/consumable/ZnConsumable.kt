package tech.ccat.znitem.item.consumable

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import tech.ccat.znitem.item.AbstractZnItem
import tech.ccat.znitem.model.ItemType

abstract class ZnConsumable : AbstractZnItem() {
    override val itemType: ItemType = ItemType.CONSUMABLE
    override val reforgeable: Boolean = false
    override val enchantable: Boolean = false
    
    open val cooldownMs: Long = 1000L
    
    abstract fun consume(player: Player, item: ItemStack)
}
