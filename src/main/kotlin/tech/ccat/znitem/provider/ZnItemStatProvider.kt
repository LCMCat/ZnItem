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

        val armorItems = player.inventory.armorContents
        val mainHandItem = player.inventory.itemInMainHand
        val offHandItem = player.inventory.itemInOffHand

        for (itemStack in armorItems) {
            if (itemStack != null && ZnItemNBT.isZnItem(itemStack)) {
                if (DurabilityChecker.isLowDurability(itemStack)) continue
                addStatsFromItem(total, itemStack, combatLevel)
            }
        }

        if (shouldApplyHandItem(mainHandItem)) {
            if (DurabilityChecker.isLowDurability(mainHandItem)) return total
            addStatsFromItem(total, mainHandItem, combatLevel)
        }

        if (shouldApplyHandItem(offHandItem)) {
            if (DurabilityChecker.isLowDurability(offHandItem)) return total
            addStatsFromItem(total, offHandItem, combatLevel)
        }

        return total
    }

    private fun shouldApplyHandItem(item: ItemStack): Boolean {
        if (item.type.isAir) return false
        if (!ZnItemNBT.isZnItem(item)) return false
        
        val itemId = ZnItemNBT.getItemId(item) ?: return false
        val znItemEnum = ZnItemEnum.fromId(itemId) ?: return false
        val znItem = znItemEnum.createItem()
        
        return !znItem.itemType.isArmor()
    }

    private fun addStatsFromItem(total: PlayerStat, itemStack: ItemStack, combatLevel: Int) {
        val itemId = ZnItemNBT.getItemId(itemStack) ?: return
        val znItemEnum = ZnItemEnum.fromId(itemId) ?: return
        val znItem = znItemEnum.createItem()
        val stats = znItem.calculateStats(itemStack, combatLevel)
        total.addAllStats(stats)
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
