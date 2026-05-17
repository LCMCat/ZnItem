package tech.ccat.znitem.nbt

import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import tech.ccat.znitem.ZnItem
import tech.ccat.znitem.model.GemQuality
import tech.ccat.znitem.model.GemType
import tech.ccat.znitem.model.ReforgeType
import tech.ccat.znitem.model.ZnItemEnum
import java.util.UUID

object ZnItemNBT {

    private fun plugin() = ZnItem.instance

    private fun key(key: String) = NamespacedKey(plugin(), key)

    fun isZnItem(itemStack: ItemStack): Boolean {
        val pdc = itemStack.itemMeta?.persistentDataContainer ?: return false
        return pdc.has(key(NBTKeys.ZN_ITEM_MARKER), PersistentDataType.BYTE)
    }

    fun markAsZnItem(itemStack: ItemStack): ItemStack {
        val meta = itemStack.itemMeta ?: return itemStack
        meta.persistentDataContainer.set(key(NBTKeys.ZN_ITEM_MARKER), PersistentDataType.BYTE, 1)
        itemStack.itemMeta = meta
        return itemStack
    }

    fun getItemId(itemStack: ItemStack): String? {
        val pdc = itemStack.itemMeta?.persistentDataContainer ?: return null
        return pdc.get(key(NBTKeys.ID), PersistentDataType.STRING)
    }

    fun setItemId(itemStack: ItemStack, id: String): ItemStack {
        val meta = itemStack.itemMeta ?: return itemStack
        meta.persistentDataContainer.set(key(NBTKeys.ID), PersistentDataType.STRING, id)
        itemStack.itemMeta = meta
        return itemStack
    }

    fun getZnItemEnum(itemStack: ItemStack): ZnItemEnum? {
        val id = getItemId(itemStack) ?: return null
        return ZnItemEnum.fromId(id)
    }

    fun getRename(itemStack: ItemStack): String? {
        val pdc = itemStack.itemMeta?.persistentDataContainer ?: return null
        return pdc.get(key(NBTKeys.RENAME), PersistentDataType.STRING)
    }

    fun setRename(itemStack: ItemStack, name: String?): ItemStack {
        val meta = itemStack.itemMeta ?: return itemStack
        if (name != null) {
            meta.persistentDataContainer.set(key(NBTKeys.RENAME), PersistentDataType.STRING, name)
        } else {
            meta.persistentDataContainer.remove(key(NBTKeys.RENAME))
        }
        itemStack.itemMeta = meta
        return itemStack
    }

    fun getReforgeType(itemStack: ItemStack): ReforgeType {
        val pdc = itemStack.itemMeta?.persistentDataContainer ?: return ReforgeType.NONE
        val name = pdc.get(key(NBTKeys.REFORGE_TYPE), PersistentDataType.STRING) ?: return ReforgeType.NONE
        return try { ReforgeType.valueOf(name) } catch (_: Exception) { ReforgeType.NONE }
    }

    fun setReforgeType(itemStack: ItemStack, reforgeType: ReforgeType): ItemStack {
        val meta = itemStack.itemMeta ?: return itemStack
        meta.persistentDataContainer.set(key(NBTKeys.REFORGE_TYPE), PersistentDataType.STRING, reforgeType.name)
        itemStack.itemMeta = meta
        return itemStack
    }

    fun isRefactored(itemStack: ItemStack): Boolean {
        val pdc = itemStack.itemMeta?.persistentDataContainer ?: return false
        val value = pdc.get(key(NBTKeys.REFACTORED), PersistentDataType.BYTE) ?: return false
        return value == 1.toByte()
    }

    fun setRefactored(itemStack: ItemStack, refactored: Boolean): ItemStack {
        val meta = itemStack.itemMeta ?: return itemStack
        meta.persistentDataContainer.set(key(NBTKeys.REFACTORED), PersistentDataType.BYTE, if (refactored) 1 else 0)
        itemStack.itemMeta = meta
        return itemStack
    }

    fun getEnchants(itemStack: ItemStack): Map<String, Int> {
        val pdc = itemStack.itemMeta?.persistentDataContainer ?: return emptyMap()
        val data = pdc.get(key(NBTKeys.ENCHANTS), PersistentDataType.STRING) ?: return emptyMap()
        if (data.isEmpty()) return emptyMap()
        val result = mutableMapOf<String, Int>()
        data.split(",").forEach { pair ->
            val parts = pair.split(":")
            if (parts.size == 2) {
                val level: Int = parts[1].toIntOrNull() ?: 0
                result[parts[0]] = level
            } else if (parts.isNotEmpty()) {
                result[parts[0]] = 0
            }
        }
        return result
    }

    fun setEnchants(itemStack: ItemStack, enchants: Map<String, Int>): ItemStack {
        val meta = itemStack.itemMeta ?: return itemStack
        val data = enchants.entries.joinToString(",") { "${it.key}:${it.value}" }
        meta.persistentDataContainer.set(key(NBTKeys.ENCHANTS), PersistentDataType.STRING, data)
        itemStack.itemMeta = meta
        return itemStack
    }

    fun getHotPowerBooks(itemStack: ItemStack): Int {
        val pdc = itemStack.itemMeta?.persistentDataContainer ?: return 0
        return pdc.get(key(NBTKeys.HOT_POWER_BOOKS), PersistentDataType.INTEGER) ?: 0
    }

    fun setHotPowerBooks(itemStack: ItemStack, amount: Int): ItemStack {
        val meta = itemStack.itemMeta ?: return itemStack
        meta.persistentDataContainer.set(key(NBTKeys.HOT_POWER_BOOKS), PersistentDataType.INTEGER, amount)
        itemStack.itemMeta = meta
        return itemStack
    }

    fun getUniqueId(itemStack: ItemStack): UUID? {
        val pdc = itemStack.itemMeta?.persistentDataContainer ?: return null
        val str = pdc.get(key(NBTKeys.UNIQUE_ID), PersistentDataType.STRING) ?: return null
        return try { UUID.fromString(str) } catch (_: Exception) { null }
    }

    fun setUniqueId(itemStack: ItemStack, uuid: UUID): ItemStack {
        val meta = itemStack.itemMeta ?: return itemStack
        meta.persistentDataContainer.set(key(NBTKeys.UNIQUE_ID), PersistentDataType.STRING, uuid.toString())
        itemStack.itemMeta = meta
        return itemStack
    }

    data class GemSlotData(val unlocked: Boolean, val gemType: GemType?, val gemQuality: GemQuality?)

    fun getGemSlots(itemStack: ItemStack): List<GemSlotData> {
        val pdc = itemStack.itemMeta?.persistentDataContainer ?: return emptyList()
        val data = pdc.get(key(NBTKeys.GEM_SLOTS), PersistentDataType.STRING) ?: return emptyList()
        if (data.isEmpty()) return emptyList()
        return data.split(";").map { slot ->
            val parts = slot.split(":")
            if (parts.size == 3) {
                val unlocked = parts[0] == "1"
                val gemType = try { GemType.valueOf(parts[1]) } catch (_: Exception) { null }
                val gemQuality = try { GemQuality.valueOf(parts[2]) } catch (_: Exception) { null }
                GemSlotData(unlocked, gemType, gemQuality)
            } else if (parts.size == 1) {
                GemSlotData(parts[0] == "1", null, null)
            } else {
                GemSlotData(false, null, null)
            }
        }
    }

    fun setGemSlots(itemStack: ItemStack, slots: List<GemSlotData>): ItemStack {
        val meta = itemStack.itemMeta ?: return itemStack
        val data = slots.joinToString(";") { slot ->
            val unlocked = if (slot.unlocked) "1" else "0"
            val type = slot.gemType?.name ?: "NONE"
            val quality = slot.gemQuality?.name ?: "NONE"
            "$unlocked:$type:$quality"
        }
        meta.persistentDataContainer.set(key(NBTKeys.GEM_SLOTS), PersistentDataType.STRING, data)
        itemStack.itemMeta = meta
        return itemStack
    }
}
