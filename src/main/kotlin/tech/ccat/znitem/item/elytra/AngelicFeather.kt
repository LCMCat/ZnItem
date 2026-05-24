package tech.ccat.znitem.item.elytra

import org.bukkit.Material
import tech.ccat.kstats.model.PlayerStat
import tech.ccat.znitem.model.*
import tech.ccat.znitem.skill.ItemSkill
import tech.ccat.znitem.skill.impl.SeraphicDescentSkill

class AngelicFeather : ZnElytra() {
    
    override val id: ZnItemEnum = ZnItemEnum.ANGELIC_FEATHER
    override val material: Material = Material.ELYTRA
    override val baseRarity: Rarity = Rarity.LEGENDARY
    override val baseName: String = "天使灵羽"
    override val baseStats: PlayerStat = PlayerStat().apply {
        speed = 50.0
        wisdom = 300.0
        defense = 120.0
        health = 260.0
    }
    override val description: List<String> = listOf(
        "§f由纯净的光芒与云朵的轻盈织就的羽翼。",
        "§f它轻轻颤动着，散发着柔和而圣洁的微光，天使之灵仿佛寄宿其中。"
    )
    override val skills: List<ItemSkill> = listOf(
        SeraphicDescentSkill()
    )
    override val restrictions: ItemRestriction = ItemRestriction()
    override val defaultGemSlots: Int = 0
    override val unbreakable: Boolean = true
}
