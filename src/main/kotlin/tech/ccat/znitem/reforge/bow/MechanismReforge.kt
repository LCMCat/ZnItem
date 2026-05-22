package tech.ccat.znitem.reforge.bow

import tech.ccat.kstats.model.PlayerStat
import tech.ccat.znitem.model.Rarity
import tech.ccat.znitem.model.ReforgeType
import tech.ccat.znitem.reforge.Reforge

class MechanismReforge : Reforge(ReforgeType.MECHANISM, "机关") {
    override fun getStatBonus(rarity: Rarity): PlayerStat {
        val stat = PlayerStat()
        when (rarity) {
            Rarity.COMMON -> { stat.strength = 2.0; stat.critDamage = 35.0 }
            Rarity.RARE -> { stat.strength = 4.0; stat.critDamage = 55.0 }
            Rarity.EPIC -> { stat.strength = 7.0; stat.critDamage = 65.0 }
            Rarity.LEGENDARY -> { stat.strength = 10.0; stat.critDamage = 75.0 }
            else -> { stat.strength = 15.0; stat.critDamage = 90.0 }
        }
        return stat
    }
}
