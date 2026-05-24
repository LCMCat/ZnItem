package tech.ccat.znitem.util

import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import tech.ccat.znitem.model.ItemType
import tech.ccat.znitem.model.ZnItemEnum
import tech.ccat.znitem.nbt.ZnItemNBT

object DurabilityChecker {

    fun isLowDurability(item: ItemStack): Boolean {
        if (!ZnItemNBT.isZnItem(item)) return false
        
        val itemId = ZnItemNBT.getItemId(item) ?: return false
        val znItemEnum = ZnItemEnum.fromId(itemId) ?: return false
        val znItem = znItemEnum.createItem()
        
        if (znItem.itemType == ItemType.CONSUMABLE) return false
        if (znItem.itemType == ItemType.MEMENTO) return false
        
        val meta = item.itemMeta as? Damageable ?: return false
        if (meta.isUnbreakable) return false
        
        val maxDurability = item.type.maxDurability
        if (maxDurability <= 0) return false
        
        val currentDurability = maxDurability - meta.damage
        return currentDurability <= 1
    }
    
    fun getDurabilityInfo(item: ItemStack): Pair<Int, Int>? {
        if (!ZnItemNBT.isZnItem(item)) return null
        
        val itemId = ZnItemNBT.getItemId(item) ?: return null
        val znItemEnum = ZnItemEnum.fromId(itemId) ?: return null
        val znItem = znItemEnum.createItem()
        
        if (znItem.itemType == ItemType.CONSUMABLE) return null
        if (znItem.itemType == ItemType.MEMENTO) return null
        
        val meta = item.itemMeta as? Damageable ?: return null
        if (meta.isUnbreakable) return null
        
        val maxDurability = item.type.maxDurability
        if (maxDurability <= 0) return null
        
        val current = (maxDurability - meta.damage)
        val max = maxDurability.toInt()
        return Pair(current, max)
    }
}
