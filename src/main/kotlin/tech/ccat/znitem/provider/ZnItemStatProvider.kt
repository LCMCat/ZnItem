package tech.ccat.znitem.provider

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import tech.ccat.kstats.api.StatProvider
import tech.ccat.kstats.model.PlayerStat
import tech.ccat.znitem.ZnItem
import tech.ccat.znitem.item.AbstractZnItem
import tech.ccat.znitem.model.ZnItemEnum
import tech.ccat.znitem.nbt.ZnItemNBT

class ZnItemStatProvider : StatProvider {

    override fun provideStats(player: Player): PlayerStat {
        val total = createEmptyStat()

        val combatLevel = getCombatLevel(player)

        val allItems = getAllEquippedItems(player)
        for (itemStack in allItems) {
            if (itemStack != null && ZnItemNBT.isZnItem(itemStack)) {
                val itemId = ZnItemNBT.getItemId(itemStack) ?: continue
                val znItemEnum = ZnItemEnum.fromId(itemId) ?: continue
                val znItem = znItemEnum.createItem()
                val stats = znItem.calculateStats(itemStack, combatLevel)
                addStats(total, stats)
            }
        }

        return total
    }

    private fun getAllEquippedItems(player: Player): List<ItemStack?> {
        val items = mutableListOf<ItemStack?>()
        player.inventory.armorContents.forEach { items.add(it) }
        items.add(player.inventory.itemInMainHand)
        items.add(player.inventory.itemInOffHand)
        player.inventory.contents.forEach { items.add(it) }
        return items
    }

    private fun getCombatLevel(player: Player): Int {
        val naSkillApi = ZnItem.instance.naSkillAPI ?: return 0
        return try {
            naSkillApi.getPlayerSkillLevel(player, tech.ccat.naskill.model.SkillType.COMBAT)
        } catch (_: Exception) {
            0
        }
    }

    private fun createEmptyStat(): PlayerStat {
        val stat = PlayerStat()
        stat.health = 0.0; stat.defense = 0.0; stat.strength = 0.0; stat.speed = 0.0; stat.baseDamage = 0.0
        stat.critChance = 0.0; stat.critDamage = 0.0; stat.wisdom = 0.0; stat.damageMultiplier = 0.0; stat.healing = 0.0; stat.manaRegen = 0.0
        return stat
    }

    private fun addStats(target: PlayerStat, source: PlayerStat) {
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
