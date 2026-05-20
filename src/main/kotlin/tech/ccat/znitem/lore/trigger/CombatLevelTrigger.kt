package tech.ccat.znitem.lore.trigger

import org.bukkit.entity.Player
import org.bukkit.event.Event
import tech.ccat.znitem.nbt.ZnItemNBT

class CombatLevelTrigger : LoreUpdateTrigger {
    override val triggerEvents: Set<String> = setOf("SkillLevelUpEvent")
    
    override fun shouldTrigger(eventName: String, event: Event, player: Player): Boolean {
        if (eventName != "SkillLevelUpEvent") return false
        return try {
            val skillTypeField = event.javaClass.getDeclaredField("skillType")
            skillTypeField.isAccessible = true
            val skillType = skillTypeField.get(event)
            val playerField = event.javaClass.getDeclaredField("player")
            playerField.isAccessible = true
            val eventPlayer = playerField.get(event) as? Player ?: return false
            
            skillType == tech.ccat.naskill.model.SkillType.COMBAT && eventPlayer == player
        } catch (_: Exception) {
            false
        }
    }
    
    override fun getAffectedSlots(player: Player): Set<Int> {
        return getAllZnItemSlots(player)
    }
    
    private fun getAllZnItemSlots(player: Player): Set<Int> {
        val slots = mutableSetOf<Int>()
        player.inventory.contents.forEachIndexed { index, item ->
            if (item != null && ZnItemNBT.isZnItem(item)) {
                slots.add(index)
            }
        }
        player.inventory.armorContents.forEachIndexed { index, item ->
            if (item != null && ZnItemNBT.isZnItem(item)) {
                slots.add(36 + index)
            }
        }
        if (ZnItemNBT.isZnItem(player.inventory.itemInOffHand)) {
            slots.add(40)
        }
        return slots
    }
}
