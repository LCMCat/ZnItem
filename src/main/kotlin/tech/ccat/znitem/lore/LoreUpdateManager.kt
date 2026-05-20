package tech.ccat.znitem.lore

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.Event
import tech.ccat.znitem.ZnItem
import tech.ccat.znitem.lore.trigger.CaLevelTrigger
import tech.ccat.znitem.lore.trigger.CombatLevelTrigger
import tech.ccat.znitem.lore.trigger.LoreUpdateTrigger
import tech.ccat.znitem.nbt.ZnItemNBT
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class LoreUpdateManager {
    private val pendingUpdates = ConcurrentHashMap<UUID, MutableSet<Int>>()
    private val triggers = mutableListOf<LoreUpdateTrigger>()
    
    fun setup() {
        triggers.add(CombatLevelTrigger())
        triggers.add(CaLevelTrigger())
    }
    
    fun registerTrigger(trigger: LoreUpdateTrigger) {
        triggers.add(trigger)
    }
    
    fun markForUpdate(player: Player, slots: Set<Int>) {
        if (slots.isEmpty()) return
        pendingUpdates.getOrPut(player.uniqueId) { ConcurrentHashMap.newKeySet() }.addAll(slots)
    }
    
    fun markAllZnItemsForUpdate(player: Player) {
        val slots = mutableSetOf<Int>()
        player.inventory.contents.forEachIndexed { index, item ->
            if (item != null && ZnItemNBT.isZnItem(item)) slots.add(index)
        }
        player.inventory.armorContents.forEachIndexed { index, item ->
            if (item != null && ZnItemNBT.isZnItem(item)) slots.add(36 + index)
        }
        if (ZnItemNBT.isZnItem(player.inventory.itemInOffHand)) slots.add(40)
        markForUpdate(player, slots)
    }
    
    fun processPendingUpdates(player: Player) {
        val slots = pendingUpdates.remove(player.uniqueId) ?: return
        if (slots.isEmpty()) return
        
        Bukkit.getScheduler().runTask(ZnItem.instance, Runnable {
            for (slot in slots) {
                val item = when {
                    slot < 36 -> player.inventory.getItem(slot)
                    slot < 40 -> player.inventory.armorContents[slot - 36]
                    slot == 40 -> player.inventory.itemInOffHand
                    else -> null
                }
                if (item != null && ZnItemNBT.isZnItem(item)) {
                    LoreRenderer.updateItemLore(item, player)
                }
            }
            player.updateInventory()
        })
    }
    
    fun checkTriggers(eventName: String, event: Event, player: Player) {
        for (trigger in triggers) {
            if (eventName in trigger.triggerEvents && trigger.shouldTrigger(eventName, event, player)) {
                val slots = trigger.getAffectedSlots(player)
                markForUpdate(player, slots)
                break
            }
        }
    }
    
    fun clearPendingUpdates(player: Player) {
        pendingUpdates.remove(player.uniqueId)
    }
}
