package tech.ccat.znitem.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import tech.ccat.znitem.ZnItem

class LoreUpdateListener : Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        ZnItem.instance.loreUpdateManager.markAllZnItemsForUpdate(event.player)
        ZnItem.instance.loreUpdateManager.processPendingUpdates(event.player)
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerQuit(event: PlayerQuitEvent) {
        ZnItem.instance.loreUpdateManager.clearPendingUpdates(event.player)
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onItemHeld(event: PlayerItemHeldEvent) {
        ZnItem.instance.loreUpdateManager.processPendingUpdates(event.player)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onInventoryOpen(event: InventoryOpenEvent) {
        val player = event.player as? org.bukkit.entity.Player ?: return
        ZnItem.instance.loreUpdateManager.processPendingUpdates(player)
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    fun onSkillLevelUp(event: tech.ccat.naskill.event.SkillLevelUpEvent) {
        ZnItem.instance.loreUpdateManager.markAllZnItemsForUpdate(event.player)
        ZnItem.instance.loreUpdateManager.processPendingUpdates(event.player)
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    fun onLevelUp(event: tech.ccat.calevel.event.LevelUpEvent) {
        ZnItem.instance.loreUpdateManager.markAllZnItemsForUpdate(event.player)
        ZnItem.instance.loreUpdateManager.processPendingUpdates(event.player)
    }
}
