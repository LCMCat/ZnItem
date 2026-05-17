package tech.ccat.znitem.listener

import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Zombie
import org.bukkit.entity.Skeleton
import org.bukkit.entity.Wither
import org.bukkit.entity.Phantom
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.entity.Player
import tech.ccat.znitem.ZnItem
import tech.ccat.znitem.enchant.ZnEnchantRegistry
import tech.ccat.znitem.model.ZnItemEnum
import tech.ccat.znitem.nbt.ZnItemNBT

class EnchantDamageListener : Listener {

    private val undeadTypes = setOf(
        org.bukkit.entity.EntityType.ZOMBIE,
        org.bukkit.entity.EntityType.SKELETON,
        org.bukkit.entity.EntityType.WITHER,
        org.bukkit.entity.EntityType.PHANTOM,
        org.bukkit.entity.EntityType.HUSK,
        org.bukkit.entity.EntityType.STRAY,
        org.bukkit.entity.EntityType.WITHER_SKELETON,
        org.bukkit.entity.EntityType.ZOMBIE_VILLAGER,
        org.bukkit.entity.EntityType.DROWNED,
        org.bukkit.entity.EntityType.ZOGLIN,
        org.bukkit.entity.EntityType.SKELETON_HORSE,
        org.bukkit.entity.EntityType.ZOMBIE_HORSE
    )

    @EventHandler(priority = EventPriority.LOW)
    fun onEntityDamage(event: EntityDamageByEntityEvent) {
        val damager = event.damager as? Player ?: return
        val target = event.entity as? LivingEntity ?: return

        val item = damager.inventory.itemInMainHand
        if (!ZnItemNBT.isZnItem(item)) return

        val enchants = ZnItemNBT.getEnchants(item)
        val smiteLevel = enchants["SMITE"] ?: return
        if (smiteLevel <= 0) return

        if (target.type !in undeadTypes) return

        val smiteEnchant = ZnEnchantRegistry.get("SMITE") ?: return
        val bonusPercent = smiteEnchant.getEffectValue(smiteLevel) / 100.0
        val bonusDamage = event.damage * bonusPercent

        event.damage = event.damage + bonusDamage
    }
}
