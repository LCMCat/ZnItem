package tech.ccat.znitem.item.sword

import org.bukkit.Material
import tech.ccat.kstats.model.PlayerStat
import tech.ccat.znitem.model.*
import tech.ccat.znitem.skill.ItemSkill
import tech.ccat.znitem.skill.impl.HelloWorldSkill
import tech.ccat.znitem.skill.impl.SpeedBoostSkill

class AlphaSword : ZnSword() {
    override val id: ZnItemEnum = ZnItemEnum.ALPHA_SWORD
    override val material: Material = Material.WOODEN_SWORD
    override val baseRarity: Rarity = Rarity.SPECIAL
    override val baseName: String = "Alpha之鱼"
    override val baseStats: PlayerStat = PlayerStat().apply {
        health = 1.0; defense = 1.0; strength = 1.0; speed = 1.0
        critChance = 1.0; critDamage = 1.0; wisdom = 1.0
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
