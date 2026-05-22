package tech.ccat.znitem.reforge.sword

import tech.ccat.kstats.model.PlayerStat
import tech.ccat.znitem.model.Rarity
import tech.ccat.znitem.model.ReforgeType
import tech.ccat.znitem.reforge.Reforge

class InfernalReforge : Reforge(ReforgeType.INFERNAL, "烈火") {
    override fun getStatBonus(rarity: Rarity): PlayerStat {
        val stat = PlayerStat()
        when (rarity) {
            Rarity.COMMON -> { stat.strength = 5.0; stat.critChance = 1.0; stat.critDamage = 10.0 }
            Rarity.RARE -> { stat.strength = 10.0; stat.critChance = 1.0; stat.critDamage = 30.0 }
            Rarity.EPIC -> { stat.strength = 15.0; stat.critChance = 1.0; stat.critDamage = 40.0 }
            Rarity.LEGENDARY -> { stat.strength = 20.0; stat.critChance = 1.0; stat.critDamage = 50.0 }
            else -> { stat.strength = 25.0; stat.critChance = 1.0; stat.critDamage = 60.0 }
        }
        return stat
    }
}
