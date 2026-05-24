package tech.ccat.znitem.item.consumable

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import tech.ccat.znitem.model.Rarity
import tech.ccat.znitem.model.ZnItemEnum
import tech.ccat.znitem.skill.ItemSkill

class GluttonousPufferfish : ZnConsumable() {
    
    override val id: ZnItemEnum = ZnItemEnum.GLUTTONOUS_PUFFERFISH
    override val material: Material = Material.PUFFERFISH
    override val baseRarity: Rarity = Rarity.EPIC
    override val baseName: String = "饕餮河豚"
    override val baseStats: tech.ccat.kstats.model.PlayerStat = tech.ccat.kstats.model.PlayerStat()
    override val description: List<String> = listOf(
        "§7食用后立即回满饱食度。",
        "§7美味与毒性共存的禁忌美食。"
    )
    override val skills: List<ItemSkill> = emptyList()
    override val restrictions: tech.ccat.znitem.model.ItemRestriction = tech.ccat.znitem.model.ItemRestriction()
    override val defaultGemSlots: Int = 0
    override val requiresUuid: Boolean = false
    
    override val cooldownMs: Long = 5000L
    
    override fun consume(player: Player, item: ItemStack) {
        player.foodLevel = 20
        player.saturation = 20f
        
        player.addPotionEffect(PotionEffect(PotionEffectType.POISON, 60, 1))
        
        item.amount -= 1
        
        player.sendMessage("§a你食用了饕餮河豚，饱食度已回满！")
    }
}
