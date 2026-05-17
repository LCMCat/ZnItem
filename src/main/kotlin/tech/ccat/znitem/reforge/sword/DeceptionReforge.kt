package tech.ccat.znitem.reforge.sword

import tech.ccat.kstats.model.PlayerStat
import tech.ccat.znitem.model.Rarity
import tech.ccat.znitem.model.ReforgeType
import tech.ccat.znitem.reforge.Reforge

class DeceptionReforge : Reforge(ReforgeType.DECEPTION, "谎言") {
    override fun getStatBonus(rarity: Rarity): PlayerStat {
        val stat = PlayerStat()
        stat.health = 0.0; stat.defense = 0.0; stat.strength = 0.0; stat.speed = 0.0; stat.baseDamage = 0.0
        stat.critChance = 0.0; stat.critDamage = 0.0; stat.wisdom = 0.0; stat.damageMultiplier = 0.0; stat.healing = 0.0; stat.manaRegen = 0.0
        when (rarity) {
            Rarity.COMMON -> stat.critDamage = 5.0
            Rarity.RARE -> stat.critDamage = 10.0
            Rarity.EPIC -> stat.critDamage = 15.0
            Rarity.LEGENDARY -> stat.critDamage = 20.0
            Rarity.MYTHIC -> stat.critDamage = 25.0
            else -> stat.critDamage = 30.0
        }
        return stat
    }
}
