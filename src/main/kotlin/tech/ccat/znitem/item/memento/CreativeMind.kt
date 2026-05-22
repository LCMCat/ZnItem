package tech.ccat.znitem.item.memento

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import tech.ccat.znitem.ZnItem
import tech.ccat.znitem.model.Rarity
import tech.ccat.znitem.model.ZnItemEnum
import tech.ccat.znitem.nbt.ZnItemNBT
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

class CreativeMind private constructor(
    private val fromName: String,
    private val fromPrefix: String?,
    private val toName: String,
    private val toPrefix: String?,
    private val contribution: String,
    private val edition: Int,
    private val dateStr: String
) : ZnMemento() {
    
    override val id: ZnItemEnum = ZnItemEnum.CREATIVE_MIND
    override val material: Material = Material.PAINTING
    override val baseRarity: Rarity = Rarity.SPECIAL
    override val baseName: String = "创造之画"
    override val baseStats: tech.ccat.kstats.model.PlayerStat = tech.ccat.kstats.model.PlayerStat()
    override val description: List<String> = listOf(
        "§7原创、远见、创造力和创新精神！",
        "§7我甚至会加上巧妙，聪明！杰作！"
    )
    override val skills: List<tech.ccat.znitem.skill.ItemSkill> = emptyList()
    override val restrictions: tech.ccat.znitem.model.ItemRestriction = tech.ccat.znitem.model.ItemRestriction()
    override val defaultGemSlots: Int = 0
    
    companion object {
        fun create(): CreativeMind = CreativeMind(
            fromName = "未知",
            fromPrefix = null,
            toName = "未知",
            toPrefix = null,
            contribution = "无",
            edition = 0,
            dateStr = "1970/01/01"
        )
        
        fun create(
            fromName: String,
            fromPrefix: String?,
            toName: String,
            toPrefix: String?,
            contribution: String,
            edition: Int
        ): CreativeMind = CreativeMind(
            fromName = fromName,
            fromPrefix = fromPrefix,
            toName = toName,
            toPrefix = toPrefix,
            contribution = contribution,
            edition = edition,
            dateStr = SimpleDateFormat("yyyy/MM/dd").format(Date())
        )
    }
    
    override fun getItemStack(): ItemStack {
        val item = ItemStack(material)
        val meta = item.itemMeta ?: return item
        
        ZnItemNBT.markAsZnItem(item)
        ZnItemNBT.setItemId(item, id.name)
        ZnItemNBT.setUniqueId(item, UUID.randomUUID())
        ZnItemNBT.setHotPowerBooks(item, 0)
        ZnItemNBT.setReforgeType(item, tech.ccat.znitem.model.ReforgeType.NONE)
        ZnItemNBT.setRefactored(item, false)
        ZnItemNBT.setEnchants(item, emptyMap())
        ZnItemNBT.setGemSlots(item, emptyList())
        
        meta.setDisplayName("${baseRarity.color}$baseName")
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE)
        
        val fromDisplay = if (fromPrefix != null) "$fromPrefix $fromName" else fromName
        val toDisplay = if (toPrefix != null) "$toPrefix $toName" else toName
        
        val lore = mutableListOf<String>()
        lore.addAll(description)
        lore.add("§7")
        lore.add("§7从: $fromDisplay")
        lore.add("§7赠送给: $toDisplay")
        lore.add("§7")
        lore.add("§7贡献: $contribution")
        lore.add("§7")
        lore.add("§8#$edition $baseName")
        lore.add("§8$dateStr")
        meta.lore = lore
        
        val pdc = meta.persistentDataContainer
        pdc.set(NamespacedKey(ZnItem.instance, "edition"), PersistentDataType.INTEGER, edition)
        pdc.set(NamespacedKey(ZnItem.instance, "from_name"), PersistentDataType.STRING, fromName)
        pdc.set(NamespacedKey(ZnItem.instance, "to_name"), PersistentDataType.STRING, toName)
        pdc.set(NamespacedKey(ZnItem.instance, "contribution"), PersistentDataType.STRING, contribution)
        pdc.set(NamespacedKey(ZnItem.instance, "date"), PersistentDataType.STRING, dateStr)
        
        item.itemMeta = meta
        return item
    }
}
