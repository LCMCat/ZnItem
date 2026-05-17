package tech.ccat.znitem.model

enum class ReforgeType(val displayName: String, val applicableTypes: Set<ItemType>) {
    INFERNAL("烈火", ItemType.WEAPON_TYPES),
    DECEPTION("谎言", setOf(ItemType.SWORD, ItemType.AXE, ItemType.DRILL)),
    WEATHERED("风化", setOf(ItemType.SWORD)),
    MECHANISM("机关", setOf(ItemType.BOW)),
    RIGID("刚硬", setOf(ItemType.BOW)),
    NONE("无", emptySet())
}
