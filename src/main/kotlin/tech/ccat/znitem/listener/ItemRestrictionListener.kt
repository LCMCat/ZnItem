package tech.ccat.znitem.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import tech.ccat.znitem.util.RestrictionChecker

class ItemRestrictionListener : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.action == Action.PHYSICAL) return
        
        val item = event.item ?: return
        val result = RestrictionChecker.checkRestrictions(event.player, item)
        if (result != null) {
            event.isCancelled = true
            RestrictionChecker.sendMessageWithCooldown(event.player, result)
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onEntityDamage(event: EntityDamageByEntityEvent) {
        val damager = event.damager as? org.bukkit.entity.Player ?: return
        val item = damager.inventory.itemInMainHand
        
        val result = RestrictionChecker.checkRestrictions(damager, item)
        if (result != null) {
            event.isCancelled = true
            RestrictionChecker.sendMessageWithCooldown(damager, result)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onItemHeld(event: PlayerItemHeldEvent) {
        val item = event.player.inventory.getItem(event.newSlot) ?: return
        
        val result = RestrictionChecker.checkRestrictions(event.player, item)
        if (result != null) {
            RestrictionChecker.sendMessageWithCooldown(event.player, result)
        }
    }
}
