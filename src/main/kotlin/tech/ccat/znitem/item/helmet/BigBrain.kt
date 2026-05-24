package tech.ccat.znitem.item.helmet

import com.destroystokyo.paper.profile.PlayerProfile
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.meta.SkullMeta
import tech.ccat.kstats.model.PlayerStat
import tech.ccat.znitem.model.*
import java.net.URL
import java.util.*

class BigBrain : ZnHelmet() {
    
    override val id: ZnItemEnum = ZnItemEnum.BIG_BRAIN
    override val material: Material = Material.PLAYER_HEAD
    override val baseRarity: Rarity = Rarity.SPECIAL
    override val baseName: String = "大聪明"
    override val baseStats: PlayerStat = PlayerStat().apply {
        wisdom = 100000.0
    }
    override val description: List<String> = listOf(
        "§7菜鸟 vs 高手 vs 黑客 vs 上帝"
    )
    override val skills: List<tech.ccat.znitem.skill.ItemSkill> = emptyList()
    override val restrictions: ItemRestriction = ItemRestriction()
    override val defaultGemSlots: Int = 0
    override val unbreakable: Boolean = true
    
    override fun getItemStack(): org.bukkit.inventory.ItemStack {
        val item = super.getItemStack()
        
        val meta = item.itemMeta as? SkullMeta ?: return item
        
        val profile: PlayerProfile = Bukkit.createProfile(UUID.randomUUID(), "BigBrain")
        profile.setTextures(profile.textures.apply {
            skin = URL("https://textures.minecraft.net/texture/40ca32b50bbf4b2918e6b7938d428b5b6d4ed9a5dcae2996e2ae25fa702fe9d4")
        })
        
        meta.playerProfile = profile
        item.itemMeta = meta
        
        return item
    }
}
