package tech.ccat.znitem.skill.impl

import org.bukkit.entity.Player
import tech.ccat.znitem.model.SkillTriggerType
import tech.ccat.znitem.skill.ItemSkill

class HelloWorldSkill : ItemSkill(
    id = "hello_world",
    name = "你好世界！",
    description = listOf("向世界发出第一声问候！"),
    manaCost = 20.0,
    cooldownSeconds = 1,
    triggerType = SkillTriggerType.RIGHT_CLICK
) {
    override fun execute(player: Player) {
        player.sendMessage("§aHello World!")
    }
}
