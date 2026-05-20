package tech.ccat.znitem.lore

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import tech.ccat.znitem.lore.component.*
import tech.ccat.znitem.nbt.ZnItemNBT

object LoreRenderer {
    private val components = mutableListOf<LoreComponent>()
    
    fun setup() {
        components.clear()
        components.add(StatsComponent())
        components.add(GemSlotsComponent())
        components.add(EnchantsComponent())
        components.add(DescriptionComponent())
        components.add(SkillsComponent())
        components.add(ReforgeAbilityComponent())
        components.add(RestrictionComponent())
        components.add(RarityFooterComponent())
        components.sortBy { it.priority }
    }
    
    fun render(context: LoreContext): List<String> {
        val lore = mutableListOf<String>()
        for (component in components) {
            if (component.shouldDisplay(context)) {
                lore.addAll(component.render(context))
            }
        }
        return lore
    }
    
    fun renderItemLore(itemStack: ItemStack, player: Player?): List<String> {
        val context = LoreContext.create(player, itemStack) ?: return emptyList()
        return render(context)
    }
    
    fun updateItemLore(itemStack: ItemStack, player: Player?) {
        if (!ZnItemNBT.isZnItem(itemStack)) return
        
        val context = LoreContext.create(player, itemStack) ?: return
        val lore = render(context)
        
        val meta = itemStack.itemMeta ?: return
        meta.lore = lore
        itemStack.itemMeta = meta
    }
}
