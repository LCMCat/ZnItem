package tech.ccat.znitem.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemDamageEvent
import org.bukkit.inventory.meta.Damageable
import tech.ccat.znitem.nbt.ZnItemNBT

class DurabilityProtectionListener : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun onItemDamage(event: PlayerItemDamageEvent) {
        val item = event.item
        if (!ZnItemNBT.isZnItem(item)) return
        
        val meta = item.itemMeta as? Damageable ?: return
        if (meta.isUnbreakable) return
        
        val currentDurability = item.type.maxDurability - meta.damage
        val damage = event.damage
        
        if (currentDurability - damage <= 0) {
            event.isCancelled = true
            meta.damage = (item.type.maxDurability - 1)
            item.itemMeta = meta
        }
    }
}
