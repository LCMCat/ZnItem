package tech.ccat.znitem.item.helmet

import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.inventory.meta.LeatherArmorMeta
import tech.ccat.kstats.model.PlayerStat
import tech.ccat.znitem.model.*
import tech.ccat.znitem.skill.ItemSkill
import tech.ccat.znitem.skill.impl.MusicPlayerSkill

class WildernessHelmet : ZnHelmet() {
    
    override val id: ZnItemEnum = ZnItemEnum.WILDERNESS_HELMET
    override val material: Material = Material.LEATHER_HELMET
    override val baseRarity: Rarity = Rarity.SPECIAL
    override val baseName: String = "旷野头盔"
    override val baseStats: PlayerStat = PlayerStat()
    override val description: List<String> = listOf(
        "§d旷野的诗歌，承载着神秘仙女的灵魂。",
        "§d它的欢快而轻盈，却带着天生的谨慎与睿智。"
    )
    override val skills: List<ItemSkill> = listOf(
        MusicPlayerSkill.getInstance("Abstract Ringing.nbs", fadeOutTicks = 20)
    )
    override val restrictions: ItemRestriction = ItemRestriction()
    override val defaultGemSlots: Int = 0
    override val unbreakable: Boolean = true
    
    override fun getItemStack(): org.bukkit.inventory.ItemStack {
        val item = super.getItemStack()
        
        val meta = item.itemMeta as? LeatherArmorMeta ?: return item
        meta.setColor(Color.fromRGB(255, 182, 193))
        item.itemMeta = meta
        
        return item
    }
}
