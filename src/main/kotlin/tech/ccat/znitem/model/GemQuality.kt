package tech.ccat.znitem.model

enum class GemQuality(val displayName: String, val rarity: Rarity) {
    ROUGH("粗糙", Rarity.COMMON),
    FLAWED("瑕疵", Rarity.RARE),
    FINE("优良", Rarity.EPIC),
    FLAWLESS("无暇", Rarity.LEGENDARY),
    PERFECT("完美", Rarity.MYTHIC)
}
