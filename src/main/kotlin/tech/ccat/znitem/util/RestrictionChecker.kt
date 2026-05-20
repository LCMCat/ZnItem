package tech.ccat.znitem.util

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import tech.ccat.znitem.ZnItem
import tech.ccat.znitem.model.ZnItemEnum
import tech.ccat.znitem.nbt.ZnItemNBT

object RestrictionChecker {
    
    private val messageCooldown = mutableMapOf<String, Long>()
    private val cooldownMs = 1000L
    
    fun checkRestrictions(player: Player, item: ItemStack): RestrictionResult? {
        if (!ZnItemNBT.isZnItem(item)) return null
        
        val itemId = ZnItemNBT.getItemId(item) ?: return null
        val znItemEnum = ZnItemEnum.fromId(itemId) ?: return null
        val znItem = znItemEnum.createItem()
        val restrictions = znItem.restrictions
        
        if (restrictions.combatLevel > 0) {
            val combatLevel = getCombatLevel(player)
            if (combatLevel < restrictions.combatLevel) {
                return RestrictionResult(
                    type = RestrictionType.COMBAT_LEVEL,
                    required = restrictions.combatLevel,
                    current = combatLevel,
                    message = "§c你需要 §4${restrictions.combatLevel}级战斗等级 §c才可使用该物品!"
                )
            }
        }
        
        if (restrictions.caLevel > 0) {
            val caLevel = getCaLevel(player)
            if (caLevel < restrictions.caLevel) {
                return RestrictionResult(
                    type = RestrictionType.CA_LEVEL,
                    required = restrictions.caLevel,
                    current = caLevel,
                    message = "§c你需要 §4${restrictions.caLevel}级全局等级 §c才可使用该物品!"
                )
            }
        }
        
        return null
    }
    
    fun sendMessageWithCooldown(player: Player, result: RestrictionResult): Boolean {
        val cooldownKey = "${player.uniqueId}:${result.type.name}"
        val lastTime = messageCooldown[cooldownKey] ?: 0L
        val now = System.currentTimeMillis()
        
        if (now - lastTime < cooldownMs) {
            return false
        }
        
        messageCooldown[cooldownKey] = now
        player.sendMessage(result.message)
        return true
    }
    
    fun canUse(player: Player, item: ItemStack): Boolean {
        return checkRestrictions(player, item) == null
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

enum class RestrictionType {
    COMBAT_LEVEL,
    CA_LEVEL
}

data class RestrictionResult(
    val type: RestrictionType,
    val required: Int,
    val current: Int,
    val message: String
)
