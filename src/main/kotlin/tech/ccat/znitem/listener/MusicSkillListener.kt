package tech.ccat.znitem.listener

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.ItemStack
import tech.ccat.znitem.ZnItem
import tech.ccat.znitem.nbt.ZnItemNBT
import tech.ccat.znitem.skill.impl.MusicPlayerSkill

class MusicSkillListener : Listener {

    private val helmetMaterials = setOf(
        Material.LEATHER_HELMET,
        Material.CHAINMAIL_HELMET,
        Material.IRON_HELMET,
        Material.DIAMOND_HELMET,
        Material.NETHERITE_HELMET,
        Material.GOLDEN_HELMET,
        Material.TURTLE_HELMET
    )

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onInventoryClick(event: InventoryClickEvent) {
        if (event.whoClicked !is Player) return
        val player = event.whoClicked as Player
        
        val isHelmetSlot = event.slot == 39
        val isShiftClick = event.click.isShiftClick
        val clickedItem = event.currentItem
        val isHelmetItem = clickedItem != null && clickedItem.type in helmetMaterials
        val cursorItem = event.cursor
        val isCursorHelmet = cursorItem != null && cursorItem.type in helmetMaterials
        
        if (isHelmetSlot || (isShiftClick && isHelmetItem) || isCursorHelmet) {
            checkHelmetChangeDelayed(player)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onInventoryDrag(event: InventoryDragEvent) {
        if (event.whoClicked !is Player) return
        val player = event.whoClicked as Player
        
        val hasHelmet = event.newItems.values.any { it.type in helmetMaterials }
        if (hasHelmet) {
            checkHelmetChangeDelayed(player)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onSwapHand(event: PlayerSwapHandItemsEvent) {
        checkHelmetChangeDelayed(event.player)
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        Bukkit.getScheduler().runTaskLater(ZnItem.instance, Runnable {
            checkAndStartMusic(event.player)
        }, 10L)
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerQuit(event: PlayerQuitEvent) {
        stopMusic(event.player)
    }

    private fun checkHelmetChangeDelayed(player: Player) {
        val previousHelmet = player.inventory.helmet?.clone()
        
        Bukkit.getScheduler().runTaskLater(ZnItem.instance, Runnable {
            val currentHelmet = player.inventory.helmet
            
            val previousWasWilderness = isWildernessHelmet(previousHelmet)
            val currentIsWilderness = isWildernessHelmet(currentHelmet)
            
            when {
                !previousWasWilderness && currentIsWilderness -> startMusic(player)
                previousWasWilderness && !currentIsWilderness -> stopMusic(player)
            }
        }, 2L)
    }

    private fun checkAndStartMusic(player: Player) {
        val helmet = player.inventory.helmet
        if (isWildernessHelmet(helmet)) {
            startMusic(player)
        }
    }

    private fun isWildernessHelmet(item: ItemStack?): Boolean {
        if (item == null || item.type == Material.AIR) return false
        if (!ZnItemNBT.isZnItem(item)) return false
        
        val itemId = ZnItemNBT.getItemId(item) ?: return false
        return itemId == "WILDERNESS_HELMET"
    }

    private fun startMusic(player: Player) {
        val skill = MusicPlayerSkill.getInstance("Abstract Ringing.nbs", fadeOutTicks = 20)
        skill.startPlaying(player)
    }

    private fun stopMusic(player: Player) {
        val skill = MusicPlayerSkill.getInstance("Abstract Ringing.nbs", fadeOutTicks = 20)
        skill.stopPlaying(player)
    }
}
