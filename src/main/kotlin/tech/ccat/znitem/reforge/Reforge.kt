package tech.ccat.znitem.reforge

import tech.ccat.kstats.model.PlayerStat
import tech.ccat.znitem.lore.LoreContext
import tech.ccat.znitem.model.Rarity
import tech.ccat.znitem.model.ReforgeType

abstract class Reforge(
    val reforgeType: ReforgeType,
    val displayName: String
) {
    abstract fun getStatBonus(rarity: Rarity): PlayerStat
    
    open fun hasSpecialAbility(): Boolean = false
    
    open fun getSpecialAbilityDescription(): String? = null
    
    open fun renderAbilityLore(context: LoreContext): List<String> {
        if (!hasSpecialAbility()) return emptyList()
        val lines = mutableListOf<String>()
        lines.add("§9${displayName}加成")
        getSpecialAbilityDescription()?.let { lines.add("§7$it") }
        lines.add("§7")
        return lines
    }
}
