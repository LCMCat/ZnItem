package tech.ccat.znitem.gem

import tech.ccat.kstats.model.PlayerStat
import tech.ccat.kstats.model.StatType
import tech.ccat.znitem.model.GemQuality
import tech.ccat.znitem.model.GemType
import tech.ccat.znitem.model.Rarity

object GemBonusCalculator {

    private val rubyBonuses = mapOf(
        GemQuality.ROUGH to mapOf(Rarity.COMMON to 1, Rarity.RARE to 3, Rarity.EPIC to 4, Rarity.LEGENDARY to 5, Rarity.MYTHIC to 7),
        GemQuality.FLAWED to mapOf(Rarity.COMMON to 3, Rarity.RARE to 5, Rarity.EPIC to 6, Rarity.LEGENDARY to 8, Rarity.MYTHIC to 10),
        GemQuality.FINE to mapOf(Rarity.COMMON to 4, Rarity.RARE to 6, Rarity.EPIC to 8, Rarity.LEGENDARY to 10, Rarity.MYTHIC to 14),
        GemQuality.FLAWLESS to mapOf(Rarity.COMMON to 5, Rarity.RARE to 10, Rarity.EPIC to 14, Rarity.LEGENDARY to 18, Rarity.MYTHIC to 22),
        GemQuality.PERFECT to mapOf(Rarity.COMMON to 6, Rarity.RARE to 13, Rarity.EPIC to 18, Rarity.LEGENDARY to 24, Rarity.MYTHIC to 30)
    )

    private val sapphireBonuses = mapOf(
        GemQuality.ROUGH to mapOf(Rarity.COMMON to 2, Rarity.RARE to 4, Rarity.EPIC to 5, Rarity.LEGENDARY to 6, Rarity.MYTHIC to 7),
        GemQuality.FLAWED to mapOf(Rarity.COMMON to 5, Rarity.RARE to 7, Rarity.EPIC to 8, Rarity.LEGENDARY to 10, Rarity.MYTHIC to 10),
        GemQuality.FINE to mapOf(Rarity.COMMON to 7, Rarity.RARE to 9, Rarity.EPIC to 10, Rarity.LEGENDARY to 11, Rarity.MYTHIC to 12),
        GemQuality.FLAWLESS to mapOf(Rarity.COMMON to 10, Rarity.RARE to 12, Rarity.EPIC to 14, Rarity.LEGENDARY to 17, Rarity.MYTHIC to 20),
        GemQuality.PERFECT to mapOf(Rarity.COMMON to 12, Rarity.RARE to 17, Rarity.EPIC to 20, Rarity.LEGENDARY to 24, Rarity.MYTHIC to 30)
    )

    private val tourmalineBonuses = mapOf(
        GemQuality.ROUGH to mapOf(Rarity.RARE to 3, Rarity.EPIC to 4, Rarity.LEGENDARY to 4, Rarity.MYTHIC to 4),
        GemQuality.FLAWED to mapOf(Rarity.RARE to 4, Rarity.EPIC to 5, Rarity.LEGENDARY to 5, Rarity.MYTHIC to 5),
        GemQuality.FINE to mapOf(Rarity.RARE to 5, Rarity.EPIC to 6, Rarity.LEGENDARY to 7, Rarity.MYTHIC to 7),
        GemQuality.FLAWLESS to mapOf(Rarity.RARE to 7, Rarity.EPIC to 8, Rarity.LEGENDARY to 10, Rarity.MYTHIC to 12),
        GemQuality.PERFECT to mapOf(Rarity.RARE to 9, Rarity.EPIC to 11, Rarity.LEGENDARY to 13, Rarity.MYTHIC to 16)
    )

    private val agateBonuses = mapOf(
        GemQuality.ROUGH to mapOf(Rarity.COMMON to 1, Rarity.RARE to 2, Rarity.EPIC to 3, Rarity.LEGENDARY to 4),
        GemQuality.FLAWED to mapOf(Rarity.COMMON to 2, Rarity.RARE to 3, Rarity.EPIC to 4, Rarity.LEGENDARY to 6),
        GemQuality.FINE to mapOf(Rarity.COMMON to 3, Rarity.RARE to 5, Rarity.EPIC to 6, Rarity.LEGENDARY to 8),
        GemQuality.FLAWLESS to mapOf(Rarity.COMMON to 5, Rarity.RARE to 7, Rarity.EPIC to 8, Rarity.LEGENDARY to 10),
        GemQuality.PERFECT to mapOf(Rarity.COMMON to 6, Rarity.RARE to 8, Rarity.EPIC to 10, Rarity.LEGENDARY to 12)
    )

    fun calculateBonus(gemType: GemType, quality: GemQuality, gemRarity: Rarity): Double {
        val bonusMap = when (gemType) {
            GemType.RUBY -> rubyBonuses
            GemType.AMETHYST -> rubyBonuses
            GemType.SAPPHIRE -> sapphireBonuses
            GemType.TOURMALINE -> tourmalineBonuses
            GemType.AGATE -> agateBonuses
        }
        val qualityMap = bonusMap[quality] ?: return 0.0
        return (qualityMap[gemRarity] ?: qualityMap.entries.firstOrNull()?.value ?: 0).toDouble()
    }

    fun calculateStatBonus(gemType: GemType, quality: GemQuality, gemRarity: Rarity): PlayerStat {
        val bonus = calculateBonus(gemType, quality, gemRarity)
        val stat = PlayerStat()
        when (gemType.statType) {
            StatType.HEALTH -> stat.health = bonus
            StatType.DEFENSE -> stat.defense = bonus
            StatType.STRENGTH -> stat.strength = bonus
            StatType.WISDOM -> stat.wisdom = bonus
            StatType.CRIT_DAMAGE -> stat.critDamage = bonus
            else -> {}
        }
        return stat
    }
}
