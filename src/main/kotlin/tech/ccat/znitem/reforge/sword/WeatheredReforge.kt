package tech.ccat.znitem.reforge.sword

import tech.ccat.kstats.model.PlayerStat
import tech.ccat.znitem.model.Rarity
import tech.ccat.znitem.model.ReforgeType
import tech.ccat.znitem.reforge.Reforge

class WeatheredReforge : Reforge(ReforgeType.WEATHERED, "风化") {
    override fun getStatBonus(rarity: Rarity): PlayerStat {
        val stat = PlayerStat()
        stat.health = 0.0; stat.defense = 0.0; stat.strength = 0.0; stat.speed = 0.0; stat.baseDamage = 0.0
        stat.critChance = 0.0; stat.critDamage = 0.0; stat.wisdom = 0.0; stat.damageMultiplier = 0.0; stat.healing = 0.0; stat.manaRegen = 0.0
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
}
