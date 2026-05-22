package tech.ccat.znitem.listener

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import tech.ccat.znitem.util.DurabilityChecker
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
            return
        }
        
        if (DurabilityChecker.isLowDurability(item)) {
            event.isCancelled = true
            sendLowDurabilityMessage(event.player, item)
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
            return
        }
        
        if (DurabilityChecker.isLowDurability(item)) {
            event.isCancelled = true
            sendLowDurabilityMessage(damager, item)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onItemHeld(event: PlayerItemHeldEvent) {
        val item = event.player.inventory.getItem(event.newSlot) ?: return
        
        val result = RestrictionChecker.checkRestrictions(event.player, item)
        if (result != null) {
            RestrictionChecker.sendMessageWithCooldown(event.player, result)
            return
        }
        
        if (DurabilityChecker.isLowDurability(item)) {
            sendLowDurabilityMessage(event.player, item)
        }
    }
    
    private val messageCooldown = mutableMapOf<String, Long>()
    private val cooldownMs = 1000L
    
    private fun sendLowDurabilityMessage(player: org.bukkit.entity.Player, item: org.bukkit.inventory.ItemStack) {
        val key = "${player.uniqueId}:low_durability"
        val lastTime = messageCooldown[key] ?: 0L
        val now = System.currentTimeMillis()
        
        if (now - lastTime < cooldownMs) return
        
        messageCooldown[key] = now
        val displayName = item.itemMeta?.displayName()
        val itemName = if (displayName != null) {
            PlainTextComponentSerializer.plainText().serialize(displayName)
        } else {
            item.type.name
        }
        player.sendMessage("§c你的 §6$itemName §c耐久即将耗尽，无法使用！")
    }
}
