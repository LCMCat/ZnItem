package tech.ccat.znitem.api

import org.bukkit.inventory.ItemStack
import tech.ccat.kstats.model.PlayerStat
import tech.ccat.znitem.ZnItem
import tech.ccat.znitem.item.AbstractZnItem
import tech.ccat.znitem.model.ZnItemEnum
import tech.ccat.znitem.nbt.ZnItemNBT

class ZnItemAPIImpl(private val plugin: ZnItem) : ZnItemAPI {

    override fun createItemStack(itemEnum: ZnItemEnum): ItemStack {
        val znItem = itemEnum.createItem()
        return znItem.getItemStack()
    }

    override fun isZnItem(itemStack: ItemStack): Boolean {
        return ZnItemNBT.isZnItem(itemStack)
    }

    override fun getZnItem(itemStack: ItemStack): AbstractZnItem? {
        val itemId = ZnItemNBT.getItemId(itemStack) ?: return null
        val znItemEnum = ZnItemEnum.fromId(itemId) ?: return null
        return znItemEnum.createItem()
    }

    override fun calculateStats(itemStack: ItemStack, combatLevel: Int): PlayerStat {
        val znItem = getZnItem(itemStack) ?: return PlayerStat()
        return znItem.calculateStats(itemStack, combatLevel)
    }

    override fun updateItemMeta(itemStack: ItemStack) {
        val znItem = getZnItem(itemStack) ?: return
        znItem.updateItemMeta(itemStack)
    }
}
