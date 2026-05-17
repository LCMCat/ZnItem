package tech.ccat.znitem.model

enum class GemType(val displayName: String, val color: String, val statType: tech.ccat.kstats.model.StatType) {
    RUBY("红宝石", "§c", tech.ccat.kstats.model.StatType.HEALTH),
    AMETHYST("紫水晶", "§5", tech.ccat.kstats.model.StatType.DEFENSE),
    SAPPHIRE("蓝宝石", "§9", tech.ccat.kstats.model.StatType.WISDOM),
    TOURMALINE("碧玺", "§a", tech.ccat.kstats.model.StatType.STRENGTH),
    AGATE("玛瑙", "§6", tech.ccat.kstats.model.StatType.CRIT_DAMAGE)
}
