package tech.ccat.znitem.util

import tech.ccat.znitem.model.Rarity

object RarityFormatter {

    fun formatName(rarity: Rarity, name: String): String {
        return "${rarity.color}$name"
    }

    fun formatRarityTag(rarity: Rarity, itemType: String): String {
        return "${rarity.color}${rarity.displayName}之${itemType}"
    }

    fun formatRefactoredTag(rarity: Rarity, itemType: String): String {
        return "${rarity.color}${rarity.displayName}之${itemType} §7(已重构)"
    }
}
