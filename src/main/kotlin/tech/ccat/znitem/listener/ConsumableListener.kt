package tech.ccat.znitem.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.block.Action
import org.bukkit.Material
import tech.ccat.znitem.item.consumable.ZnConsumable
import tech.ccat.znitem.model.ItemType
import tech.ccat.znitem.model.ZnItemEnum
import tech.ccat.znitem.nbt.ZnItemNBT

class ConsumableListener : Listener {

    private val cooldowns = mutableMapOf<String, Long>()

    @EventHandler(priority = EventPriority.HIGH)
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val action = event.action
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return
        
        val player = event.player
        val item = player.inventory.itemInMainHand
        
        if (item.type == Material.AIR) return
        if (!ZnItemNBT.isZnItem(item)) return
        
        val itemId = ZnItemNBT.getItemId(item) ?: return
        val znItemEnum = ZnItemEnum.fromId(itemId) ?: return
        val znItem = znItemEnum.createItem()
        
        if (znItem.itemType != ItemType.CONSUMABLE) return
        
        event.isCancelled = true
        
        val consumable = znItem as ZnConsumable
        
        val cooldownKey = "${player.uniqueId}:$itemId"
        val cooldownMs = consumable.cooldownMs
        val lastUse = cooldowns[cooldownKey] ?: 0L
        val now = System.currentTimeMillis()
        
        if (now - lastUse < cooldownMs) {
            val remaining = ((cooldownMs - (now - lastUse)) / 1000.0)
            player.sendMessage("§c冷却中，请等待 ${String.format("%.1f", remaining)} 秒")
            return
        }
        
        cooldowns[cooldownKey] = now
        
        consumable.consume(player, item)
    }
}
