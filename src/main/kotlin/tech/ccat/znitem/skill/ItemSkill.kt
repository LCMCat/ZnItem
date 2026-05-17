package tech.ccat.znitem.skill

import org.bukkit.entity.Player
import tech.ccat.znitem.model.SkillTriggerType

abstract class ItemSkill(
    val id: String,
    val name: String,
    val description: List<String>,
    val manaCost: Double,
    val cooldownSeconds: Long,
    val triggerType: SkillTriggerType
) {
    abstract fun execute(player: Player)

    open fun shouldCancelOriginalEvent(): Boolean {
        return triggerType == SkillTriggerType.RIGHT_CLICK ||
                triggerType == SkillTriggerType.THROW
    }
}
