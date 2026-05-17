package tech.ccat.znitem.item.gem

import org.bukkit.Material
import tech.ccat.kstats.model.PlayerStat
import tech.ccat.znitem.item.AbstractZnItem
import tech.ccat.znitem.model.*

abstract class ZnGem : AbstractZnItem() {
    override val itemType: ItemType = ItemType.GEM
    override val reforgeable: Boolean = false
    override val enchantable: Boolean = false
    override val material: Material = Material.PLAYER_HEAD
    abstract val gemType: GemType
    abstract val gemQuality: GemQuality
}
