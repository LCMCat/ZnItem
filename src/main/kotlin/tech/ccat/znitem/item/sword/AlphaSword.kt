package tech.ccat.znitem.item.sword

import org.bukkit.Material
import tech.ccat.kstats.model.PlayerStat
import tech.ccat.znitem.item.AbstractZnItem
import tech.ccat.znitem.model.*
import tech.ccat.znitem.skill.ItemSkill
import tech.ccat.znitem.skill.impl.HelloWorldSkill
import tech.ccat.znitem.skill.impl.SpeedBoostSkill

class AlphaSword : ZnSword() {
    override val id: ZnItemEnum = ZnItemEnum.ALPHA_SWORD
    override val material: Material = Material.WOODEN_SWORD
    override val baseRarity: Rarity = Rarity.SPECIAL
    override val baseName: String = "Alpha之鱼"
    override val baseStats: PlayerStat = run {
        val stat = PlayerStat()
        stat.health = 0.0; stat.defense = 0.0; stat.strength = 0.0; stat.speed = 0.0; stat.baseDamage = 0.0
        stat.critChance = 0.0; stat.critDamage = 0.0; stat.wisdom = 0.0; stat.damageMultiplier = 0.0; stat.healing = 0.0; stat.manaRegen = 0.0
        stat.health = 1.0; stat.defense = 1.0; stat.strength = 1.0; stat.speed = 1.0
        stat.critChance = 1.0; stat.critDamage = 1.0; stat.wisdom = 1.0
        stat
    }
    override val description: List<String> = listOf("§cAlpha测试咸鱼...")
    override val skills: List<ItemSkill> = listOf(
        HelloWorldSkill(),
        SpeedBoostSkill()
    )
    override val restrictions: ItemRestriction = ItemRestriction(combatLevel = 1, caLevel = 1)
    override val defaultGemSlots: Int = 3
    override val defaultUnlockedGemSlots: Int = 3
}
