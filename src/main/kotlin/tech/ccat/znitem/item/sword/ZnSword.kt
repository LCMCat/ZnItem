package tech.ccat.znitem.item.sword

import org.bukkit.Material
import tech.ccat.kstats.model.PlayerStat
import tech.ccat.znitem.item.AbstractZnItem
import tech.ccat.znitem.model.ItemType
import tech.ccat.znitem.model.Rarity
import tech.ccat.znitem.model.ZnItemEnum

abstract class ZnSword : AbstractZnItem() {
    override val itemType: ItemType = ItemType.SWORD
    override val reforgeable: Boolean = true
    override val enchantable: Boolean = true
}
