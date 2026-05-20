package tech.ccat.znitem.lore.component

import tech.ccat.znitem.lore.LoreComponent
import tech.ccat.znitem.lore.LoreContext

class DescriptionComponent : LoreComponent {
    override val priority: Int = 40
    
    override fun shouldDisplay(context: LoreContext): Boolean = context.znItem.description.isNotEmpty()
    
    override fun render(context: LoreContext): List<String> {
        if (context.znItem.description.isEmpty()) return emptyList()
        
        val lines = mutableListOf<String>()
        lines.add("§7")
        lines.addAll(context.znItem.description)
        return lines
    }
}
