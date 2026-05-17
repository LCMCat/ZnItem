package tech.ccat.znitem.reforge

import tech.ccat.kstats.model.PlayerStat
import tech.ccat.znitem.model.Rarity
import tech.ccat.znitem.model.ReforgeType

abstract class Reforge(
    val reforgeType: ReforgeType,
    val displayName: String
) {
    abstract fun getStatBonus(rarity: Rarity): PlayerStat

    open fun hasSpecialAbility(): Boolean = false

    open fun getSpecialAbilityDescription(): String? = null
}
