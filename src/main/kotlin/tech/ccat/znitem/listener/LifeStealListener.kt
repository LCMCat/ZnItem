package tech.ccat.znitem.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.entity.Player
import tech.ccat.znitem.ZnItem
import tech.ccat.znitem.enchant.ZnEnchantRegistry
import tech.ccat.znitem.nbt.ZnItemNBT

class LifeStealListener : Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onAttack(event: EntityDamageByEntityEvent) {
        val damager = event.damager as? Player ?: return
        
        val item = damager.inventory.itemInMainHand
        if (!ZnItemNBT.isZnItem(item)) return
        
        val enchants = ZnItemNBT.getEnchants(item)
        val lifeStealLevel = enchants["LIFE_STEAL"] ?: return
        if (lifeStealLevel <= 0) return
        
        val enchant = ZnEnchantRegistry.get("LIFE_STEAL") ?: return
        val healPercent = enchant.getEffectValue(lifeStealLevel) / 100.0
        
        val kstatsAPI = ZnItem.instance.kstatsAPI ?: return
        val maxHealth = kstatsAPI.getMaxHealth(damager)
        val healAmount = maxHealth * healPercent
        
        kstatsAPI.healPlayer(damager, healAmount)
    }
}
