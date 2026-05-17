package tech.ccat.znitem.enchant

import tech.ccat.znitem.model.ItemType

class SmiteEnchant : ZnEnchant(
    id = "SMITE",
    displayName = "亡灵杀手",
    applicableTypes = ItemType.ENCHANT_WEAPON_TYPES
) {
    override fun getEffectValue(level: Int): Double {
        if (level <= 0) return 0.0
        if (level <= 4) return 5.0 + (level - 1) * 5.0
        return 30.0 + (level - 5) * 10.0
    }

    override fun getDescription(level: Int): String {
        return "增加亡灵伤害 ${getEffectValue(level)}%"
    }
}
