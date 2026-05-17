package tech.ccat.znitem.provider

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import tech.ccat.kstats.api.StatProvider
import tech.ccat.kstats.model.PlayerStat
import tech.ccat.znitem.nbt.ZnItemNBT

class VanillaItemProvider : StatProvider {

    private val weaponDamage = mapOf(
        Material.WOODEN_SWORD to 4.0,
        Material.STONE_SWORD to 5.0,
        Material.IRON_SWORD to 6.0,
        Material.DIAMOND_SWORD to 7.0,
        Material.NETHERITE_SWORD to 8.0,
        Material.WOODEN_AXE to 7.0,
        Material.STONE_AXE to 9.0,
        Material.IRON_AXE to 9.0,
        Material.DIAMOND_AXE to 9.0,
        Material.NETHERITE_AXE to 10.0,
        Material.BOW to 6.0,
        Material.CROSSBOW to 6.0,
        Material.TRIDENT to 9.0,
        Material.WOODEN_PICKAXE to 2.0,
        Material.STONE_PICKAXE to 2.0,
        Material.IRON_PICKAXE to 2.0,
        Material.DIAMOND_PICKAXE to 2.0,
        Material.NETHERITE_PICKAXE to 3.0,
        Material.WOODEN_SHOVEL to 2.5,
        Material.STONE_SHOVEL to 2.5,
        Material.IRON_SHOVEL to 2.5,
        Material.DIAMOND_SHOVEL to 2.5,
        Material.NETHERITE_SHOVEL to 3.5,
        Material.WOODEN_HOE to 1.0,
        Material.STONE_HOE to 1.0,
        Material.IRON_HOE to 1.0,
        Material.DIAMOND_HOE to 1.0,
        Material.NETHERITE_HOE to 2.0
    )

    private val armorDefense = mapOf(
        Material.LEATHER_HELMET to 1.0,
        Material.LEATHER_CHESTPLATE to 3.0,
        Material.LEATHER_LEGGINGS to 2.0,
        Material.LEATHER_BOOTS to 1.0,
        Material.CHAINMAIL_HELMET to 2.0,
        Material.CHAINMAIL_CHESTPLATE to 5.0,
        Material.CHAINMAIL_LEGGINGS to 4.0,
        Material.CHAINMAIL_BOOTS to 1.0,
        Material.IRON_HELMET to 2.0,
        Material.IRON_CHESTPLATE to 6.0,
        Material.IRON_LEGGINGS to 5.0,
        Material.IRON_BOOTS to 2.0,
        Material.DIAMOND_HELMET to 3.0,
        Material.DIAMOND_CHESTPLATE to 8.0,
        Material.DIAMOND_LEGGINGS to 6.0,
        Material.DIAMOND_BOOTS to 3.0,
        Material.NETHERITE_HELMET to 3.0,
        Material.NETHERITE_CHESTPLATE to 8.0,
        Material.NETHERITE_LEGGINGS to 6.0,
        Material.NETHERITE_BOOTS to 3.0,
        Material.TURTLE_HELMET to 2.0
    )

    override fun provideStats(player: Player): PlayerStat {
        val total = PlayerStat()
        total.health = 0.0; total.defense = 0.0; total.strength = 0.0; total.speed = 0.0; total.baseDamage = 0.0
        total.critChance = 0.0; total.critDamage = 0.0; total.wisdom = 0.0; total.damageMultiplier = 0.0; total.healing = 0.0; total.manaRegen = 0.0

        val allItems = mutableListOf<ItemStack?>()
        player.inventory.armorContents.forEach { allItems.add(it) }
        allItems.add(player.inventory.itemInMainHand)
        allItems.add(player.inventory.itemInOffHand)

        for (item in allItems) {
            if (item == null || item.type == Material.AIR) continue
            if (ZnItemNBT.isZnItem(item)) continue

            weaponDamage[item.type]?.let { total.baseDamage += it }
            armorDefense[item.type]?.let { total.defense += it }
        }

        return total
    }
}
