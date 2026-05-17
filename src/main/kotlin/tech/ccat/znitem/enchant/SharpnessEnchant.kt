package tech.ccat.znitem.enchant

import tech.ccat.znitem.model.ItemType

class SharpnessEnchant : ZnEnchant(
    id = "SHARPNESS",
    displayName = "锋利",
    applicableTypes = ItemType.ENCHANT_WEAPON_TYPES
) {
    override fun getEffectValue(level: Int): Double {
        if (level <= 0) return 0.0
        if (level <= 4) return 5.0 + (level - 1) * 5.0
        return 30.0 + (level - 5) * 10.0
    }

    override fun getDescription(level: Int): String {
        return "增加物品BASE DAMAGE ${getEffectValue(level)}%"
    }
}
