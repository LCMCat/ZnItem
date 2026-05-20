package tech.ccat.znitem.lore.component

import tech.ccat.znitem.lore.LoreComponent
import tech.ccat.znitem.lore.LoreContext

class GemSlotsComponent : LoreComponent {
    override val priority: Int = 20
    
    override fun shouldDisplay(context: LoreContext): Boolean = context.gemSlots.isNotEmpty()
    
    override fun render(context: LoreContext): List<String> {
        if (context.gemSlots.isEmpty()) return emptyList()
        
        val line = StringBuilder()
        for (slot in context.gemSlots) {
            when {
                slot.hasGem && slot.gemType != null -> {
                    line.append("${slot.gemType.color}[⚛]")
                }
                slot.unlocked -> {
                    line.append("§7[⚛]")
                }
                else -> {
                    line.append("§9[⚛]")
                }
            }
        }
        return listOf(line.toString())
    }
}
