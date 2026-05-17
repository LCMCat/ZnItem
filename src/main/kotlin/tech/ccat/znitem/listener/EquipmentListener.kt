package tech.ccat.znitem.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import tech.ccat.znitem.ZnItem

class EquipmentListener : Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    fun onInventoryClick(event: InventoryClickEvent) {
        if (event.whoClicked !is org.bukkit.entity.Player) return
        val player = event.whoClicked as org.bukkit.entity.Player
        if (event.slotType == org.bukkit.event.inventory.InventoryType.SlotType.ARMOR ||
            event.slot == 36 || event.slot == 37 || event.slot == 38 || event.slot == 39 ||
            event.slot == 40 || event.rawSlot == 45) {
            requestUpdateDelayed(player)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onItemHeld(event: PlayerItemHeldEvent) {
        requestUpdateDelayed(event.player)
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onSwapHand(event: PlayerSwapHandItemsEvent) {
        requestUpdateDelayed(event.player)
    }

    private fun requestUpdateDelayed(player: org.bukkit.entity.Player) {
        org.bukkit.Bukkit.getScheduler().runTaskLater(ZnItem.instance, Runnable {
            ZnItem.instance.dataManager.refreshPlayer(player)
            ZnItem.instance.kstatsAPI?.requestUpdate(player)
        }, 1L)
    }
}
