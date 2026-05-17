package tech.ccat.znitem.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import tech.ccat.znitem.ZnItem
import tech.ccat.znitem.nbt.ZnItemNBT

class PlayerJoinListener : Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        ZnItem.instance.dataManager.loadPlayer(player)

        val allItems = player.inventory.contents.filterNotNull() +
                player.inventory.armorContents.filterNotNull() +
                listOfNotNull(if (player.inventory.itemInOffHand.type != org.bukkit.Material.AIR) player.inventory.itemInOffHand else null)

        for (item in allItems) {
            if (ZnItemNBT.isZnItem(item)) {
                val uuid = ZnItemNBT.getUniqueId(item)
                if (uuid != null) {
                    ZnItem.instance.uuidRegistry.register(uuid, player.uniqueId)
                }
            }
        }

        org.bukkit.Bukkit.getScheduler().runTaskLater(ZnItem.instance, Runnable {
            ZnItem.instance.kstatsAPI?.requestUpdate(player)
        }, 10L)
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player
        ZnItem.instance.backupManager.backupPlayer(player, "QUIT")
        ZnItem.instance.uuidRegistry.unregisterAllForPlayer(player.uniqueId)
        ZnItem.instance.dataManager.unloadPlayer(player)
    }
}
