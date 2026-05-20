package tech.ccat.znitem.lore.trigger

import org.bukkit.entity.Player
import org.bukkit.event.Event
import tech.ccat.znitem.nbt.ZnItemNBT

class CaLevelTrigger : LoreUpdateTrigger {
    override val triggerEvents: Set<String> = setOf("PlayerLevelUpEvent", "LevelUpEvent")
    
    override fun shouldTrigger(eventName: String, event: Event, player: Player): Boolean {
        if (eventName !in triggerEvents) return false
        return try {
            val playerField = event.javaClass.getDeclaredField("player")
            playerField.isAccessible = true
            val eventPlayer = playerField.get(event) as? Player ?: return false
            eventPlayer == player
        } catch (_: Exception) {
            try {
                val uuidField = event.javaClass.getDeclaredField("playerUuid")
                uuidField.isAccessible = true
                val uuid = uuidField.get(event) as? java.util.UUID ?: return false
                uuid == player.uniqueId
            } catch (_: Exception) {
                false
            }
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
