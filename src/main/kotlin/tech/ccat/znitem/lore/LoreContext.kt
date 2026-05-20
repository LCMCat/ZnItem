package tech.ccat.znitem.lore

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import tech.ccat.znitem.ZnItem
import tech.ccat.znitem.gem.GemSlot
import tech.ccat.znitem.gem.GemSlotManager
import tech.ccat.znitem.item.AbstractZnItem
import tech.ccat.znitem.model.Rarity
import tech.ccat.znitem.model.ReforgeType
import tech.ccat.znitem.model.ZnItemEnum
import tech.ccat.znitem.nbt.ZnItemNBT

data class LoreContext(
    val player: Player?,
    val itemStack: ItemStack,
    val znItem: AbstractZnItem,
    val combatLevel: Int,
    val caLevel: Int,
    val effectiveRarity: Rarity,
    val reforgeType: ReforgeType,
    val enchants: Map<String, Int>,
    val hotPowerBooks: Int,
    val refactored: Boolean,
    val gemSlots: List<GemSlot>
) {
    companion object {
        fun create(player: Player?, itemStack: ItemStack): LoreContext? {
            if (!ZnItemNBT.isZnItem(itemStack)) return null
            
            val itemId = ZnItemNBT.getItemId(itemStack) ?: return null
            val znItemEnum = ZnItemEnum.fromId(itemId) ?: return null
            val znItem = znItemEnum.createItem()
            
            val combatLevel = player?.let { getCombatLevel(it) } ?: 0
            val caLevel = player?.let { getCaLevel(it) } ?: 0
            
            return LoreContext(
                player = player,
                itemStack = itemStack,
                znItem = znItem,
                combatLevel = combatLevel,
                caLevel = caLevel,
                effectiveRarity = znItem.getEffectiveRarity(itemStack),
                reforgeType = ZnItemNBT.getReforgeType(itemStack),
                enchants = ZnItemNBT.getEnchants(itemStack),
                hotPowerBooks = ZnItemNBT.getHotPowerBooks(itemStack),
                refactored = ZnItemNBT.isRefactored(itemStack),
                gemSlots = GemSlotManager.getGemSlots(itemStack)
            )
        }
        
        private fun getCombatLevel(player: Player): Int {
            val naSkillApi = ZnItem.instance.naSkillAPI ?: return 0
            return try {
                naSkillApi.getPlayerSkillLevel(player, tech.ccat.naskill.model.SkillType.COMBAT)
            } catch (_: Exception) {
                0
            }
        }
        
        private fun getCaLevel(player: Player): Int {
            val caLevelApi = ZnItem.instance.caLevelAPI ?: return 0
            return try {
                caLevelApi.getPlayerLevelData(player)?.level ?: 0
            } catch (_: Exception) {
                0
            }
        }
    }
}
