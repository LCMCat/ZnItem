package tech.ccat.znitem.lore.component

import tech.ccat.kstats.model.PlayerStat
import tech.ccat.znitem.enchant.ZnEnchantRegistry
import tech.ccat.znitem.lore.LoreComponent
import tech.ccat.znitem.lore.LoreContext
import tech.ccat.znitem.model.ReforgeType
import tech.ccat.znitem.reforge.ReforgeRegistry

class StatsComponent : LoreComponent {
    override val priority: Int = 10
    
    override fun shouldDisplay(context: LoreContext): Boolean = true
    
    override fun render(context: LoreContext): List<String> {
        val lines = mutableListOf<String>()
        val baseStats = context.znItem.baseStats
        val reforgeBonus = getReforgeBonus(context)
        
        lines.add("§7")
        
        renderDamageLine(lines, context, baseStats, reforgeBonus)
        renderStrengthLine(lines, context, baseStats, reforgeBonus)
        renderStatLine(lines, "生命", "§c", baseStats.health, reforgeBonus?.health ?: 0.0)
        renderStatLine(lines, "防御", "§9", baseStats.defense, reforgeBonus?.defense ?: 0.0)
        renderStatLine(lines, "速度", "§3", baseStats.speed, reforgeBonus?.speed ?: 0.0)
        renderStatLine(lines, "暴击几率", "§d", baseStats.critChance, reforgeBonus?.critChance ?: 0.0, "%")
        renderStatLine(lines, "暴击伤害", "§5", baseStats.critDamage, reforgeBonus?.critDamage ?: 0.0, "%")
        renderStatLine(lines, "智慧", "§3", baseStats.wisdom, reforgeBonus?.wisdom ?: 0.0)
        
        return lines
    }
    
    private fun getReforgeBonus(context: LoreContext): PlayerStat? {
        if (context.reforgeType == ReforgeType.NONE) return null
        return ReforgeRegistry.get(context.reforgeType)?.getStatBonus(context.effectiveRarity)
    }
    
    private fun renderDamageLine(
        lines: MutableList<String>,
        context: LoreContext,
        baseStats: PlayerStat,
        reforgeBonus: PlayerStat?
    ) {
        val baseDmg = baseStats.baseDamage
        val hpbDmg = if (context.hotPowerBooks > 0 && context.znItem.itemType.isWeapon()) 
            context.hotPowerBooks * 2 else 0
        val sharpLevel = context.enchants["SHARPNESS"] ?: 0
        val sharpBonus = if (sharpLevel > 0) {
            ZnEnchantRegistry.get("SHARPNESS")?.getEffectValue(sharpLevel) ?: 0.0
        } else 0.0
        
        val totalDmg = baseDmg + hpbDmg + baseDmg * sharpBonus / 100.0
        if (totalDmg <= 0 && baseDmg <= 0) return
        
        val detailParts = mutableListOf<String>()
        if (baseDmg > 0) detailParts.add("${baseDmg.toInt()}基础")
        if (hpbDmg > 0) detailParts.add("${hpbDmg}炙能之书")
        if (sharpBonus > 0) detailParts.add("${sharpBonus.toInt()}%锋利${tech.ccat.znitem.enchant.ZnEnchant.toRoman(sharpLevel)}")
        
        if (detailParts.isNotEmpty()) {
            lines.add("§7伤害: §c+${formatStat(totalDmg)}§8(${detailParts.joinToString(" + ")})")
        }
    }
    
    private fun renderStrengthLine(
        lines: MutableList<String>,
        context: LoreContext,
        baseStats: PlayerStat,
        reforgeBonus: PlayerStat?
    ) {
        val base = baseStats.strength
        val reforge = reforgeBonus?.strength ?: 0.0
        val hpbBonus = if (context.hotPowerBooks > 0 && context.znItem.itemType.isWeapon()) 
            context.hotPowerBooks * 2 else 0
        val weatheredBonus = if (context.reforgeType == ReforgeType.WEATHERED) context.combatLevel else 0
        
        if (base <= 0 && reforge <= 0 && hpbBonus <= 0 && weatheredBonus <= 0) return
        
        val total = base + reforge + hpbBonus + weatheredBonus
        val detailParts = mutableListOf<String>()
        if (base > 0) detailParts.add("${base.toInt()}基础")
        if (reforge > 0) detailParts.add("${reforge.toInt()}${ReforgeRegistry.get(context.reforgeType)?.displayName ?: ""}")
        if (hpbBonus > 0) detailParts.add("${hpbBonus}炙能之书")
        if (weatheredBonus > 0) detailParts.add("${weatheredBonus}战斗等级")
        
        if (detailParts.isNotEmpty()) {
            lines.add("§7力量: §4+${total.toInt()}§8(${detailParts.joinToString(" + ")})")
        } else {
            lines.add("§7力量: §4+${total.toInt()}")
        }
    }
    
    private fun renderStatLine(
        lines: MutableList<String>,
        name: String,
        color: String,
        base: Double,
        reforge: Double,
        suffix: String = ""
    ) {
        if (base <= 0 && reforge <= 0) return
        val total = base + reforge
        lines.add("§7$name: $color+${formatStat(total)}$suffix")
    }
    
    private fun formatStat(value: Double): String {
        return if (value == value.toLong().toDouble()) {
            String.format("%d", value.toLong())
        } else {
            String.format("%.1f", value)
        }
    }
}
