package tech.ccat.znitem.reforge.bow

import tech.ccat.kstats.model.PlayerStat
import tech.ccat.znitem.model.Rarity
import tech.ccat.znitem.model.ReforgeType
import tech.ccat.znitem.reforge.Reforge

class RigidReforge : Reforge(ReforgeType.RIGID, "刚硬") {
    override fun getStatBonus(rarity: Rarity): PlayerStat {
        val stat = PlayerStat()
        stat.health = 0.0; stat.defense = 0.0; stat.strength = 0.0; stat.speed = 0.0; stat.baseDamage = 0.0
        stat.critChance = 0.0; stat.critDamage = 0.0; stat.wisdom = 0.0; stat.damageMultiplier = 0.0; stat.healing = 0.0; stat.manaRegen = 0.0
        when (rarity) {
            Rarity.COMMON -> { stat.strength = 3.0; stat.critChance = 8.0; stat.critDamage = 5.0 }
            Rarity.RARE -> { stat.strength = 12.0; stat.critChance = 10.0; stat.critDamage = 18.0 }
            Rarity.EPIC -> { stat.strength = 18.0; stat.critChance = 11.0; stat.critDamage = 32.0 }
            Rarity.LEGENDARY -> { stat.strength = 25.0; stat.critChance = 13.0; stat.critDamage = 50.0 }
            else -> { stat.strength = 34.0; stat.critChance = 15.0; stat.critDamage = 70.0 }
        }
        return stat
    }
}
