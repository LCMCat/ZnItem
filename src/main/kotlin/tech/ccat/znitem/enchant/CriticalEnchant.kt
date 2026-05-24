package tech.ccat.znitem.enchant

import tech.ccat.znitem.model.ItemType

class CriticalEnchant : ZnEnchant(
    id = "CRITICAL",
    displayName = "暴击",
    applicableTypes = ItemType.ENCHANT_WEAPON_TYPES
) {
    override fun getEffectValue(level: Int): Double {
        return when (level) {
            1 -> 10.0
            2 -> 20.0
            3 -> 30.0
            4 -> 40.0
            5 -> 50.0
            6 -> 70.0
            7 -> 100.0
            else -> if (level > 7) 100.0 + (level - 7) * 15.0 else 0.0
        }
    }

    override fun getDescription(level: Int): String {
        return "增加暴击伤害 ${getEffectValue(level)}%"
    }
}
