package tech.ccat.znitem.lore.component

import tech.ccat.znitem.lore.LoreComponent
import tech.ccat.znitem.lore.LoreContext
import tech.ccat.znitem.model.ReforgeType
import tech.ccat.znitem.reforge.ReforgeRegistry

class ReforgeAbilityComponent : LoreComponent {
    override val priority: Int = 60
    
    override fun shouldDisplay(context: LoreContext): Boolean {
        if (context.reforgeType == ReforgeType.NONE) return false
        val reforge = ReforgeRegistry.get(context.reforgeType) ?: return false
        return reforge.hasSpecialAbility()
    }
    
    override fun render(context: LoreContext): List<String> {
        if (context.reforgeType == ReforgeType.NONE) return emptyList()
        val reforge = ReforgeRegistry.get(context.reforgeType) ?: return emptyList()
        
        return reforge.renderAbilityLore(context)
    }
}
