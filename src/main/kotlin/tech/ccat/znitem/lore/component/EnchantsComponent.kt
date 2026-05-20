package tech.ccat.znitem.lore.component

import tech.ccat.znitem.enchant.ZnEnchantRegistry
import tech.ccat.znitem.lore.LoreComponent
import tech.ccat.znitem.lore.LoreContext

class EnchantsComponent : LoreComponent {
    override val priority: Int = 30
    
    override fun shouldDisplay(context: LoreContext): Boolean = context.enchants.isNotEmpty()
    
    override fun render(context: LoreContext): List<String> {
        if (context.enchants.isEmpty()) return emptyList()
        
        val lines = mutableListOf<String>()
        lines.add("§7")
        
        for ((enchantId, level) in context.enchants) {
            val enchant = ZnEnchantRegistry.get(enchantId) ?: continue
            lines.add(enchant.formatLore(level))
        }
        
        return lines
    }
}
