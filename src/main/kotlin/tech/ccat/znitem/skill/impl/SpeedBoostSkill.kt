package tech.ccat.znitem.skill.impl

import org.bukkit.entity.Player
import tech.ccat.znitem.model.SkillTriggerType
import tech.ccat.znitem.skill.ItemSkill

class SpeedBoostSkill : ItemSkill(
    id = "speed_boost",
    name = "快一点！",
    description = listOf("高速开发中...", "后面忘了"),
    manaCost = 0.0,
    cooldownSeconds = 0,
    triggerType = SkillTriggerType.MAIN_HAND
) {
    override fun execute(player: Player) {
    }
}
