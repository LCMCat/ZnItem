package tech.ccat.znitem.api

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import tech.ccat.znitem.item.AbstractZnItem
import tech.ccat.znitem.model.ZnItemEnum

interface ZnItemAPI {
    fun createItemStack(itemEnum: ZnItemEnum): ItemStack
    fun isZnItem(itemStack: ItemStack): Boolean
    fun getZnItem(itemStack: ItemStack): AbstractZnItem?
    fun calculateStats(itemStack: ItemStack, combatLevel: Int): tech.ccat.kstats.model.PlayerStat
    fun updateItemMeta(itemStack: ItemStack)
}
