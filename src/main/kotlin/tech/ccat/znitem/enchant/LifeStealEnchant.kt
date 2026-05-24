package tech.ccat.znitem.enchant

import tech.ccat.znitem.model.ItemType

class LifeStealEnchant : ZnEnchant(
    id = "LIFE_STEAL",
    displayName = "生命偷取",
    applicableTypes = ItemType.ENCHANT_WEAPON_TYPES
) {
    override fun getEffectValue(level: Int): Double {
        if (level <= 0) return 0.0
        if (level > 5) return 2.5 + (level - 5) * 0.5
        return 0.5 * level
    }

    override fun getDescription(level: Int): String {
        return "攻击时回复最大生命值的 ${getEffectValue(level)}%"
    }
}
