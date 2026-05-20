package tech.ccat.znitem.lore.component

import tech.ccat.znitem.lore.LoreComponent
import tech.ccat.znitem.lore.LoreContext

class RarityFooterComponent : LoreComponent {
    override val priority: Int = 100
    
    override fun shouldDisplay(context: LoreContext): Boolean = true
    
    override fun render(context: LoreContext): List<String> {
        val tag = if (context.refactored) {
            "${context.effectiveRarity.color}${context.effectiveRarity.displayName}之${context.znItem.itemType.displayName} §7(已重构)"
        } else {
            "${context.effectiveRarity.color}${context.effectiveRarity.displayName}之${context.znItem.itemType.displayName}"
        }
        return listOf(tag)
    }
}
