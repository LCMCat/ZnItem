package tech.ccat.znitem.lore.component

import tech.ccat.znitem.lore.LoreComponent
import tech.ccat.znitem.lore.LoreContext

class SkillsComponent : LoreComponent {
    override val priority: Int = 50
    
    override fun shouldDisplay(context: LoreContext): Boolean = context.znItem.skills.isNotEmpty()
    
    override fun render(context: LoreContext): List<String> {
        if (context.znItem.skills.isEmpty()) return emptyList()
        
        val lines = mutableListOf<String>()
        lines.add("§7")
        
        for (skill in context.znItem.skills) {
            lines.add("§e${skill.triggerType.displayName}: ${skill.name}")
            for (descLine in skill.description) {
                lines.add("§7$descLine")
            }
            if (skill.manaCost > 0) {
                lines.add("§9法力消耗: §3${skill.manaCost.toInt()}")
            }
            if (skill.cooldownSeconds > 0) {
                lines.add("§9冷却: §a${skill.cooldownSeconds}秒")
            }
            lines.add("§7")
        }
        
        return lines
    }
}
