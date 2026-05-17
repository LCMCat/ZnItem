package tech.ccat.znitem.util

import org.bukkit.inventory.ItemStack
import tech.ccat.kstats.model.StatType
import tech.ccat.znitem.ZnItem
import tech.ccat.znitem.enchant.ZnEnchantRegistry
import tech.ccat.znitem.gem.GemSlotManager
import tech.ccat.znitem.item.AbstractZnItem
import tech.ccat.znitem.model.ItemType
import tech.ccat.znitem.model.Rarity
import tech.ccat.znitem.model.ReforgeType
import tech.ccat.znitem.nbt.ZnItemNBT
import tech.ccat.znitem.reforge.ReforgeRegistry

object ItemLoreBuilder {

    fun buildLore(znItem: AbstractZnItem, itemStack: ItemStack): List<String> {
        val lore = mutableListOf<String>()
        val effectiveRarity = znItem.getEffectiveRarity(itemStack)
        val reforgeType = ZnItemNBT.getReforgeType(itemStack)
        val enchants = ZnItemNBT.getEnchants(itemStack)
        val hotPowerBooks = ZnItemNBT.getHotPowerBooks(itemStack)
        val refactored = ZnItemNBT.isRefactored(itemStack)
        val combatLevel = getCombatLevel()

        lore.add("§7")

        buildStatLines(lore, znItem, itemStack, reforgeType, hotPowerBooks, enchants, combatLevel, effectiveRarity)

        buildGemSlotLines(lore, itemStack)

        lore.add("§7")

        buildEnchantLines(lore, enchants)

        lore.add("§7")

        buildDescriptionLines(lore, znItem)

        lore.add("§7")

        buildSkillLines(lore, znItem)

        lore.add("§7")

        buildReforgeLines(lore, reforgeType, effectiveRarity)

        buildRestrictionLines(lore, znItem)

        buildRarityFooter(lore, effectiveRarity, znItem.itemType.displayName, refactored)

        return lore
    }

    private fun buildStatLines(
        lore: MutableList<String>,
        znItem: AbstractZnItem,
        itemStack: ItemStack,
        reforgeType: ReforgeType,
        hotPowerBooks: Int,
        enchants: Map<String, Int>,
        combatLevel: Int,
        effectiveRarity: Rarity
    ) {
        val baseStats = znItem.baseStats
        val reforgeBonus = if (reforgeType != ReforgeType.NONE) {
            val stat = tech.ccat.kstats.model.PlayerStat()
            stat.health = 0.0; stat.defense = 0.0; stat.strength = 0.0; stat.speed = 0.0; stat.baseDamage = 0.0
            stat.critChance = 0.0; stat.critDamage = 0.0; stat.wisdom = 0.0; stat.damageMultiplier = 0.0; stat.healing = 0.0; stat.manaRegen = 0.0
            ReforgeRegistry.get(reforgeType)?.getStatBonus(effectiveRarity) ?: stat
        } else null

        val hasDamage = baseStats.baseDamage > 0 || (reforgeBonus?.baseDamage ?: 0.0) > 0 || (hotPowerBooks > 0 && znItem.itemType.isWeapon())
        if (hasDamage) {
            val baseDmg = baseStats.baseDamage
            val hpbDmg = if (hotPowerBooks > 0 && znItem.itemType.isWeapon()) hotPowerBooks * 2 else 0
            val sharpLevel = enchants["SHARPNESS"] ?: 0
            val sharpBonus = if (sharpLevel > 0) {
                val enchant = ZnEnchantRegistry.get("SHARPNESS")
                enchant?.getEffectValue(sharpLevel) ?: 0.0
            } else 0.0

            val totalDmg = baseDmg + hpbDmg + baseDmg * sharpBonus / 100.0
            val detailParts = mutableListOf<String>()
            if (baseDmg > 0) detailParts.add("${baseDmg.toInt()}基础")
            if (hpbDmg > 0) detailParts.add("${hpbDmg}炙能之书")
            if (sharpBonus > 0) detailParts.add("${sharpBonus.toInt()}%锋利${tech.ccat.znitem.enchant.ZnEnchant.toRoman(sharpLevel)}")

            lore.add("§7伤害: §c+${formatStat(totalDmg)}§8(${detailParts.joinToString(" + ")})")
        }

        buildSingleStatLine(lore, "力量", "§4", baseStats.strength, reforgeBonus?.strength ?: 0.0,
            hotPowerBooks, znItem.itemType, combatLevel, reforgeType)

        buildSingleStatLineNoHPB(lore, "生命", "§c", baseStats.health, reforgeBonus?.health ?: 0.0)
        buildSingleStatLineNoHPB(lore, "防御", "§9", baseStats.defense, reforgeBonus?.defense ?: 0.0)
        buildSingleStatLineNoHPB(lore, "速度", "§3", baseStats.speed, reforgeBonus?.speed ?: 0.0)
        buildSingleStatLineNoHPB(lore, "暴击几率", "§d", baseStats.critChance, reforgeBonus?.critChance ?: 0.0, "%")
        buildSingleStatLineNoHPB(lore, "暴击伤害", "§5", baseStats.critDamage, reforgeBonus?.critDamage ?: 0.0, "%")
        buildSingleStatLineNoHPB(lore, "智慧", "§3", baseStats.wisdom, reforgeBonus?.wisdom ?: 0.0)
    }

    private fun buildSingleStatLine(
        lore: MutableList<String>,
        name: String,
        color: String,
        base: Double,
        reforge: Double,
        hotPowerBooks: Int,
        itemType: ItemType,
        combatLevel: Int,
        reforgeType: ReforgeType
    ) {
        if (base <= 0 && reforge <= 0 && hotPowerBooks <= 0) return

        val hpbBonus = if (hotPowerBooks > 0 && itemType.isWeapon()) hotPowerBooks * 2 else 0
        val weatheredBonus = if (reforgeType == ReforgeType.WEATHERED) combatLevel else 0
        val total = base + reforge + hpbBonus + weatheredBonus

        val detailParts = mutableListOf<String>()
        if (base > 0) detailParts.add("${base.toInt()}基础")
        if (reforge > 0) detailParts.add("${reforge.toInt()}${ReforgeRegistry.get(reforgeType)?.displayName ?: ""}")
        if (hpbBonus > 0) detailParts.add("${hpbBonus}炙能之书")
        if (weatheredBonus > 0) detailParts.add("${weatheredBonus}战斗等级")

        if (detailParts.isNotEmpty()) {
            lore.add("§7$name: $color+${total.toInt()}§8(${detailParts.joinToString(" + ")})")
        } else {
            lore.add("§7$name: $color+${total.toInt()}")
        }
    }

    private fun buildSingleStatLineNoHPB(
        lore: MutableList<String>,
        name: String,
        color: String,
        base: Double,
        reforge: Double,
        suffix: String = ""
    ) {
        if (base <= 0 && reforge <= 0) return
        val total = base + reforge
        lore.add("§7$name: $color+${formatStat(total)}$suffix")
    }

    private fun buildGemSlotLines(lore: MutableList<String>, itemStack: ItemStack) {
        val slots = GemSlotManager.getGemSlots(itemStack)
        if (slots.isEmpty()) return

        val line = StringBuilder()
        for (slot in slots) {
            when {
                slot.hasGem && slot.gemType != null -> {
                    line.append("${slot.gemType.color}[⚛️]")
                }
                slot.unlocked -> {
                    line.append("§7[⚛️]")
                }
                else -> {
                    line.append("§9[⚛️]")
                }
            }
        }
        lore.add(line.toString())
    }

    private fun buildEnchantLines(lore: MutableList<String>, enchants: Map<String, Int>) {
        if (enchants.isEmpty()) return
        for ((enchantId, level) in enchants) {
            val enchant = ZnEnchantRegistry.get(enchantId) ?: continue
            lore.add(enchant.formatLore(level))
        }
    }

    private fun buildDescriptionLines(lore: MutableList<String>, znItem: AbstractZnItem) {
        for (line in znItem.description) {
            lore.add(line)
        }
    }

    private fun buildSkillLines(lore: MutableList<String>, znItem: AbstractZnItem) {
        for (skill in znItem.skills) {
            lore.add("§e${skill.triggerType.displayName}: ${skill.name}")
            for (descLine in skill.description) {
                lore.add("§7$descLine")
            }
            if (skill.manaCost > 0) {
                lore.add("§9法力消耗: §3${skill.manaCost.toInt()}")
            }
            if (skill.cooldownSeconds > 0) {
                lore.add("§9冷却: §a${skill.cooldownSeconds}秒")
            }
            lore.add("§7")
        }
    }

    private fun buildReforgeLines(lore: MutableList<String>, reforgeType: ReforgeType, rarity: Rarity) {
        if (reforgeType == ReforgeType.NONE) return
        val reforge = ReforgeRegistry.get(reforgeType) ?: return
        lore.add("§9${reforge.displayName}加成")
        if (reforge.hasSpecialAbility()) {
            lore.add("§7${reforge.getSpecialAbilityDescription()}")
        }
        lore.add("§7")
    }

    private fun buildRestrictionLines(lore: MutableList<String>, znItem: AbstractZnItem) {
        if (znItem.restrictions.combatLevel > 0) {
            lore.add("§4❣ §c需要 ${znItem.restrictions.combatLevel}级 战斗等级")
        }
        if (znItem.restrictions.caLevel > 0) {
            lore.add("§4❣ §c需要 ${znItem.restrictions.caLevel}级 全局等级")
        }
    }

    private fun buildRarityFooter(lore: MutableList<String>, rarity: Rarity, typeName: String, refactored: Boolean) {
        val tag = if (refactored) {
            "${rarity.color}${rarity.displayName}之${typeName} §7(已重构)"
        } else {
            "${rarity.color}${rarity.displayName}之${typeName}"
        }
        lore.add(tag)
    }

    private fun formatStat(value: Double): String {
        return if (value == value.toLong().toDouble()) {
            String.format("%d", value.toLong())
        } else {
            String.format("%.1f", value)
        }
    }

    private fun getCombatLevel(): Int {
        return 0
    }
}
