package tech.ccat.znitem.model

enum class Rarity(
    val displayName: String,
    val color: String,
    val priority: Int
) {
    COMMON("普通", "§7", 0),
    RARE("稀有", "§9", 1),
    EPIC("史诗", "§5", 2),
    LEGENDARY("传奇", "§6", 3),
    MYTHIC("神话", "§d", 4),
    DIVINE("非凡", "§b", 5),
    SPECIAL("特殊", "§c", 6),
    ULTRA_SPECIAL("极特殊", "§4", 7);

    fun next(): Rarity? = entries.firstOrNull { it.priority == this.priority + 1 }

    fun isAtLeast(rarity: Rarity): Boolean = this.priority >= rarity.priority

    companion object {
        fun fromPriority(priority: Int): Rarity =
            entries.firstOrNull { it.priority == priority } ?: COMMON
    }
}
