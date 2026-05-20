package tech.ccat.znitem.listener

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import tech.ccat.znitem.ZnItem

class EquipmentListener : Listener {

    private val armorMaterials = setOf(
        Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS,
        Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS,
        Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS,
        Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS,
        Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS,
        Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS,
        Material.TURTLE_HELMET
    )

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onInventoryClick(event: InventoryClickEvent) {
        if (event.whoClicked !is org.bukkit.entity.Player) return
        val player = event.whoClicked as org.bukkit.entity.Player
        
        val isArmorSlot = event.slotType == org.bukkit.event.inventory.InventoryType.SlotType.ARMOR
        val isShiftClick = event.click.isShiftClick
        val clickedItem = event.currentItem
        val isArmorItem = clickedItem != null && clickedItem.type in armorMaterials
        val cursorItem = event.cursor
        val isCursorArmor = cursorItem != null && cursorItem.type in armorMaterials
        
        if (isArmorSlot || isShiftClick || isArmorItem || isCursorArmor) {
            requestUpdateDelayed(player)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onInventoryDrag(event: InventoryDragEvent) {
        if (event.whoClicked !is org.bukkit.entity.Player) return
        val player = event.whoClicked as org.bukkit.entity.Player
        
        val hasArmor = event.newItems.values.any { it.type in armorMaterials }
        if (hasArmor) {
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
