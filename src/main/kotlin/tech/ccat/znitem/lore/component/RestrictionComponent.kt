package tech.ccat.znitem.lore.component

import tech.ccat.znitem.lore.LoreComponent
import tech.ccat.znitem.lore.LoreContext

class RestrictionComponent : LoreComponent {
    override val priority: Int = 70
    
    override fun shouldDisplay(context: LoreContext): Boolean {
        val restrictions = context.znItem.restrictions
        val combatNotMet = restrictions.combatLevel > 0 && context.combatLevel < restrictions.combatLevel
        val caNotMet = restrictions.caLevel > 0 && context.caLevel < restrictions.caLevel
        return combatNotMet || caNotMet
    }
    
    override fun render(context: LoreContext): List<String> {
        val lines = mutableListOf<String>()
        val restrictions = context.znItem.restrictions
        
        if (restrictions.combatLevel > 0 && context.combatLevel < restrictions.combatLevel) {
            lines.add("§4❣ §c需要 ${restrictions.combatLevel}级 战斗等级")
        }
        if (restrictions.caLevel > 0 && context.caLevel < restrictions.caLevel) {
            lines.add("§4❣ §c需要 ${restrictions.caLevel}级 全局等级")
        }
        
        return lines
    }
}
