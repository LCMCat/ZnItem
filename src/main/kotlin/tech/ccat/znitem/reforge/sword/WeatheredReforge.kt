package tech.ccat.znitem.reforge.sword

import tech.ccat.kstats.model.PlayerStat
import tech.ccat.znitem.lore.LoreContext
import tech.ccat.znitem.model.Rarity
import tech.ccat.znitem.model.ReforgeType
import tech.ccat.znitem.reforge.Reforge

class WeatheredReforge : Reforge(ReforgeType.WEATHERED, "风化") {
    override fun getStatBonus(rarity: Rarity): PlayerStat {
        val stat = PlayerStat()
        when (rarity) {
            Rarity.COMMON -> stat.strength = 60.0
            Rarity.RARE -> stat.strength = 90.0
            Rarity.EPIC -> stat.strength = 110.0
            Rarity.LEGENDARY -> stat.strength = 135.0
            Rarity.MYTHIC -> stat.strength = 170.0
            else -> stat.strength = 210.0
        }
        return stat
    }

    override fun hasSpecialAbility(): Boolean = true

    override fun getSpecialAbilityDescription(): String =
        "对每级§c战斗等级§7, §a+1§c力量§7"
    
    override fun renderAbilityLore(context: LoreContext): List<String> {
        return listOf(
            "§9${displayName}加成",
            "§7对每级§c战斗等级§7, §a+1§c力量§7",
            "§7"
        )
    }
}
