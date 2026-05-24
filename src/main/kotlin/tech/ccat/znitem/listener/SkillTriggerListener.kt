package tech.ccat.znitem.listener

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityToggleGlideEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import tech.ccat.znitem.ZnItem
import tech.ccat.znitem.model.SkillTriggerType
import tech.ccat.znitem.model.ZnItemEnum
import tech.ccat.znitem.nbt.ZnItemNBT
import tech.ccat.znitem.util.CooldownManager
import tech.ccat.znitem.util.RestrictionChecker

class SkillTriggerListener : Listener {

    private val cooldownManager = CooldownManager()
    
    private val armorMaterials = setOf(
        Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS,
        Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS,
        Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS,
        Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS,
        Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS,
        Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS,
        Material.TURTLE_HELMET, Material.ELYTRA
    )

    @EventHandler(priority = EventPriority.HIGH)
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player
        val item = player.inventory.itemInMainHand
        if (!ZnItemNBT.isZnItem(item)) return

        if (!RestrictionChecker.canUse(player, item)) return

        val itemId = ZnItemNBT.getItemId(item) ?: return
        val znItemEnum = ZnItemEnum.fromId(itemId) ?: return
        val znItem = znItemEnum.createItem()

        val action = event.action
        for (skill in znItem.skills) {
            when (skill.triggerType) {
                SkillTriggerType.RIGHT_CLICK -> {
                    if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                        if (skill.shouldCancelOriginalEvent()) {
                            event.isCancelled = true
                        }
                        executeSkill(player, skill)
                    }
                }
                SkillTriggerType.LEFT_CLICK -> {
                    if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
                        executeSkill(player, skill)
                    }
                }
                else -> {}
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onPlayerDropItem(event: PlayerDropItemEvent) {
        val player = event.player
        val item = event.itemDrop.itemStack
        if (!ZnItemNBT.isZnItem(item)) return

        if (!RestrictionChecker.canUse(player, item)) return

        val itemId = ZnItemNBT.getItemId(item) ?: return
        val znItemEnum = ZnItemEnum.fromId(itemId) ?: return
        val znItem = znItemEnum.createItem()

        for (skill in znItem.skills) {
            if (skill.triggerType == SkillTriggerType.THROW) {
                if (skill.shouldCancelOriginalEvent()) {
                    event.isCancelled = true
                }
                executeSkill(player, skill)
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onEntityToggleGlide(event: EntityToggleGlideEvent) {
        if (event.entity !is org.bukkit.entity.Player) return
        val player = event.entity as org.bukkit.entity.Player
        
        if (!event.isGliding) return
        
        val chestplate = player.inventory.chestplate ?: return
        if (!ZnItemNBT.isZnItem(chestplate)) return
        
        val itemId = ZnItemNBT.getItemId(chestplate) ?: return
        val znItemEnum = ZnItemEnum.fromId(itemId) ?: return
        val znItem = znItemEnum.createItem()
        
        for (skill in znItem.skills) {
            if (skill.triggerType == SkillTriggerType.ELYTRA_FLIGHT) {
                executeSkill(player, skill)
            }
        }
    }

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
            checkEquipSkillsDelayed(player)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onInventoryDrag(event: InventoryDragEvent) {
        if (event.whoClicked !is org.bukkit.entity.Player) return
        val player = event.whoClicked as org.bukkit.entity.Player
        
        val hasArmor = event.newItems.values.any { it.type in armorMaterials }
        if (hasArmor) {
            checkEquipSkillsDelayed(player)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        org.bukkit.Bukkit.getScheduler().runTaskLater(ZnItem.instance, Runnable {
            checkEquipSkills(event.player)
        }, 10L)
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerQuit(event: PlayerQuitEvent) {
        stopEquipSkills(event.player)
    }

    private fun checkEquipSkillsDelayed(player: org.bukkit.entity.Player) {
        val previousHelmet = player.inventory.helmet?.clone()
        
        org.bukkit.Bukkit.getScheduler().runTaskLater(ZnItem.instance, Runnable {
            val currentHelmet = player.inventory.helmet
            
            if (!isSameItem(previousHelmet, currentHelmet)) {
                if (previousHelmet != null) {
                    triggerUnequipSkills(player, previousHelmet)
                }
                if (currentHelmet != null) {
                    triggerEquipSkills(player, currentHelmet)
                }
            }
        }, 2L)
    }

    private fun checkEquipSkills(player: org.bukkit.entity.Player) {
        val helmet = player.inventory.helmet
        if (helmet != null) {
            triggerEquipSkills(player, helmet)
        }
    }

    private fun stopEquipSkills(player: org.bukkit.entity.Player) {
        val helmet = player.inventory.helmet
        if (helmet != null) {
            triggerUnequipSkills(player, helmet)
        }
    }

    private fun isSameItem(item1: org.bukkit.inventory.ItemStack?, item2: org.bukkit.inventory.ItemStack?): Boolean {
        if (item1 == null && item2 == null) return true
        if (item1 == null || item2 == null) return false
        return item1.isSimilar(item2)
    }

    private fun triggerEquipSkills(player: org.bukkit.entity.Player, item: org.bukkit.inventory.ItemStack) {
        if (!ZnItemNBT.isZnItem(item)) return
        
        val itemId = ZnItemNBT.getItemId(item) ?: return
        val znItemEnum = ZnItemEnum.fromId(itemId) ?: return
        val znItem = znItemEnum.createItem()
        
        for (skill in znItem.skills) {
            if (skill.triggerType == SkillTriggerType.EQUIP) {
                skill.execute(player)
            }
        }
    }

    private fun triggerUnequipSkills(player: org.bukkit.entity.Player, item: org.bukkit.inventory.ItemStack) {
        if (!ZnItemNBT.isZnItem(item)) return
        
        val itemId = ZnItemNBT.getItemId(item) ?: return
        val znItemEnum = ZnItemEnum.fromId(itemId) ?: return
        val znItem = znItemEnum.createItem()
        
        for (skill in znItem.skills) {
            if (skill.triggerType == SkillTriggerType.EQUIP) {
                val stopMethod = skill.javaClass.methods.find { it.name == "stopPlaying" }
                stopMethod?.invoke(skill, player)
            }
        }
    }

    private fun executeSkill(player: org.bukkit.entity.Player, skill: tech.ccat.znitem.skill.ItemSkill) {
        if (cooldownManager.isOnCooldown(player, skill.id)) {
            val remaining = cooldownManager.getRemainingCooldown(player, skill.id)
            player.sendMessage("§c物品冷却中 (${cooldownManager.formatCooldown(remaining)})")
            return
        }

        if (skill.manaCost > 0) {
            val kstatsAPI = ZnItem.instance.kstatsAPI
            if (kstatsAPI != null) {
                if (!kstatsAPI.consumeMana(player, skill.manaCost, skill.name, true)) {
                    return
                }
            }
        }

        skill.execute(player)

        if (skill.cooldownSeconds > 0) {
            cooldownManager.setCooldown(player, skill.id, skill.cooldownSeconds)
        }
    }
}
