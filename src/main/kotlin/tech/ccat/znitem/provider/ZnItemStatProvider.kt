package tech.ccat.znitem.provider

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import tech.ccat.kstats.api.StatProvider
import tech.ccat.kstats.model.PlayerStat
import tech.ccat.znitem.ZnItem
import tech.ccat.znitem.model.ZnItemEnum
import tech.ccat.znitem.nbt.ZnItemNBT
import tech.ccat.znitem.util.DurabilityChecker

class ZnItemStatProvider : StatProvider {

    override fun provideStats(player: Player): PlayerStat {
        val total = PlayerStat()

        val combatLevel = getCombatLevel(player)

        val allItems = getAllEquippedItems(player)
        for (itemStack in allItems) {
            if (itemStack != null && ZnItemNBT.isZnItem(itemStack)) {
                if (DurabilityChecker.isLowDurability(itemStack)) continue
                
                val itemId = ZnItemNBT.getItemId(itemStack) ?: continue
                val znItemEnum = ZnItemEnum.fromId(itemId) ?: continue
                val znItem = znItemEnum.createItem()
                val stats = znItem.calculateStats(itemStack, combatLevel)
                total.addAllStats(stats)
            }
        }

        return total
    }

    private fun getAllEquippedItems(player: Player): List<ItemStack?> {
        val items = mutableListOf<ItemStack?>()
        player.inventory.armorContents.forEach { items.add(it) }
        items.add(player.inventory.itemInMainHand)
        items.add(player.inventory.itemInOffHand)
        return items
    }

    private fun getCombatLevel(player: Player): Int {
        val naSkillApi = ZnItem.instance.naSkillAPI ?: return 0
        return try {
            naSkillApi.getPlayerSkillLevel(player, tech.ccat.naskill.model.SkillType.COMBAT)
        } catch (_: Exception) {
            0
        }
    }
}
