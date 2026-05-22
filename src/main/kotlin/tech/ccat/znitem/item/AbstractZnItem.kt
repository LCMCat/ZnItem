package tech.ccat.znitem.item

import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import tech.ccat.kstats.model.PlayerStat
import tech.ccat.znitem.enchant.ZnEnchantRegistry
import tech.ccat.znitem.gem.GemBonusCalculator
import tech.ccat.znitem.gem.GemSlotManager
import tech.ccat.znitem.lore.LoreRenderer
import tech.ccat.znitem.model.*
import tech.ccat.znitem.nbt.ZnItemNBT
import tech.ccat.znitem.reforge.ReforgeRegistry
import tech.ccat.znitem.skill.ItemSkill
import java.util.UUID

abstract class AbstractZnItem {

    abstract val id: ZnItemEnum
    abstract val material: Material
    abstract val itemType: ItemType
    abstract val baseRarity: Rarity
    abstract val baseName: String
    abstract val baseStats: PlayerStat
    abstract val reforgeable: Boolean
    abstract val enchantable: Boolean
    abstract val description: List<String>
    abstract val skills: List<ItemSkill>
    abstract val restrictions: ItemRestriction
    abstract val defaultGemSlots: Int

    open val defaultUnlockedGemSlots: Int = 0

    fun getItemStack(): ItemStack {
        val item = ItemStack(material)
        val uuid = UUID.randomUUID()

        ZnItemNBT.markAsZnItem(item)
        ZnItemNBT.setItemId(item, id.name)
        ZnItemNBT.setUniqueId(item, uuid)
        ZnItemNBT.setHotPowerBooks(item, 0)
        ZnItemNBT.setReforgeType(item, ReforgeType.NONE)
        ZnItemNBT.setRefactored(item, false)
        ZnItemNBT.setEnchants(item, emptyMap())

        val gemSlots = (0 until defaultGemSlots).map { index ->
            ZnItemNBT.GemSlotData(unlocked = index < defaultUnlockedGemSlots, null, null)
        }
        ZnItemNBT.setGemSlots(item, gemSlots)

        updateItemMeta(item, null)
        return item
    }

    fun updateItemMeta(itemStack: ItemStack, player: org.bukkit.entity.Player? = null) {
        val meta = itemStack.itemMeta ?: return

        val rename = ZnItemNBT.getRename(itemStack)
        val refactored = ZnItemNBT.isRefactored(itemStack)
        val effectiveRarity = if (refactored) baseRarity.next() ?: baseRarity else baseRarity

        val displayName = if (rename != null) {
            "${effectiveRarity.color}$rename"
        } else {
            "${effectiveRarity.color}$baseName"
        }
        meta.setDisplayName(displayName)

        val enchants = ZnItemNBT.getEnchants(itemStack)
        val hasEnchant = enchants.isNotEmpty()
        if (hasEnchant) {
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE)

        val lore = LoreRenderer.renderItemLore(itemStack, player)
        meta.lore = lore

        itemStack.itemMeta = meta

        if (hasEnchant && !itemStack.enchantments.containsKey(org.bukkit.enchantments.Enchantment.UNBREAKING)) {
            itemStack.addUnsafeEnchantment(org.bukkit.enchantments.Enchantment.UNBREAKING, 1)
        }
    }

    fun calculateStats(itemStack: ItemStack, combatLevel: Int = 0): PlayerStat {
        val total = copyStat(baseStats)

        val reforgeType = ZnItemNBT.getReforgeType(itemStack)
        if (reforgeType != ReforgeType.NONE) {
            val refactored = ZnItemNBT.isRefactored(itemStack)
            val effectiveRarity = if (refactored) baseRarity.next() ?: baseRarity else baseRarity
            val reforge = ReforgeRegistry.get(reforgeType)
            reforge?.let { addStats(total, it.getStatBonus(effectiveRarity)) }

            if (reforgeType == ReforgeType.WEATHERED) {
                total.strength += combatLevel
            }
        }

        val hotPowerBooks = ZnItemNBT.getHotPowerBooks(itemStack)
        if (hotPowerBooks > 0) {
            when (itemType) {
                ItemType.SWORD, ItemType.BOW, ItemType.AXE, ItemType.WAND, ItemType.FISHING_ROD -> {
                    total.strength += hotPowerBooks * 2
                    total.baseDamage += hotPowerBooks * 2
                }
                ItemType.HELMET, ItemType.CHESTPLATE, ItemType.LEGGINGS, ItemType.BOOTS -> {
                    total.health += hotPowerBooks * 2
                    total.defense += hotPowerBooks * 2
                }
                else -> {}
            }
        }

        val enchants = ZnItemNBT.getEnchants(itemStack)
        enchants.forEach { (enchantId, level) ->
            val enchant = ZnEnchantRegistry.get(enchantId)
            if (enchant != null && itemType in enchant.applicableTypes) {
                if (enchantId == "SHARPNESS") {
                    total.baseDamage += total.baseDamage * enchant.getEffectValue(level) / 100.0
                }
            }
        }

        val gemSlots = GemSlotManager.getGemSlots(itemStack)
        for (slot in gemSlots) {
            if (slot.hasGem && slot.gemType != null && slot.gemQuality != null) {
                val gemRarity = slot.gemQuality.rarity
                val bonus = GemBonusCalculator.calculateStatBonus(slot.gemType, slot.gemQuality, gemRarity)
                addStats(total, bonus)
            }
        }

        return total
    }

    fun getEffectiveRarity(itemStack: ItemStack): Rarity {
        val refactored = ZnItemNBT.isRefactored(itemStack)
        return if (refactored) baseRarity.next() ?: baseRarity else baseRarity
    }

    companion object {
        fun copyStat(source: PlayerStat): PlayerStat = source.copy()

        fun addStats(target: PlayerStat, source: PlayerStat) {
            target.health += source.health
            target.defense += source.defense
            target.strength += source.strength
            target.speed += source.speed
            target.baseDamage += source.baseDamage
            target.critChance += source.critChance
            target.critDamage += source.critDamage
            target.wisdom += source.wisdom
            target.damageMultiplier += source.damageMultiplier
            target.healing += source.healing
            target.manaRegen += source.manaRegen
        }
    }
}
