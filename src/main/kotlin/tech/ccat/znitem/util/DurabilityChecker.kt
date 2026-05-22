package tech.ccat.znitem.util

import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import tech.ccat.znitem.nbt.ZnItemNBT

object DurabilityChecker {

    fun isLowDurability(item: ItemStack): Boolean {
        if (!ZnItemNBT.isZnItem(item)) return false
        
        val meta = item.itemMeta as? Damageable ?: return false
        if (meta.isUnbreakable) return false
        
        val currentDurability = item.type.maxDurability - meta.damage
        return currentDurability <= 1
    }
    
    fun getDurabilityInfo(item: ItemStack): Pair<Int, Int>? {
        if (!ZnItemNBT.isZnItem(item)) return null
        
        val meta = item.itemMeta as? Damageable ?: return null
        if (meta.isUnbreakable) return null
        
        val current = (item.type.maxDurability - meta.damage)
        val max = item.type.maxDurability.toInt()
        return Pair(current, max)
    }
}
