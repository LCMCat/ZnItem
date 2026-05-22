package tech.ccat.znitem.listener

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemBreakEvent
import tech.ccat.znitem.ZnItem

class ItemBreakListener : Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    fun onItemBreak(event: PlayerItemBreakEvent) {
        val player = event.player
        
        Bukkit.getScheduler().runTaskLater(ZnItem.instance, Runnable {
            ZnItem.instance.dataManager.refreshPlayer(player)
            ZnItem.instance.kstatsAPI?.requestUpdate(player)
        }, 1L)
    }
}
