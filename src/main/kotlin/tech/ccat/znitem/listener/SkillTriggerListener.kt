package tech.ccat.znitem.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerDropItemEvent
import tech.ccat.znitem.ZnItem
import tech.ccat.znitem.model.SkillTriggerType
import tech.ccat.znitem.model.ZnItemEnum
import tech.ccat.znitem.nbt.ZnItemNBT
import tech.ccat.znitem.util.CooldownManager
import tech.ccat.znitem.util.RestrictionChecker

class SkillTriggerListener : Listener {

    private val cooldownManager = CooldownManager()

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
