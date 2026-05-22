package tech.ccat.znitem.reforge.sword

import tech.ccat.kstats.model.PlayerStat
import tech.ccat.znitem.model.Rarity
import tech.ccat.znitem.model.ReforgeType
import tech.ccat.znitem.reforge.Reforge

class DeceptionReforge : Reforge(ReforgeType.DECEPTION, "谎言") {
    override fun getStatBonus(rarity: Rarity): PlayerStat {
        val stat = PlayerStat()
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
