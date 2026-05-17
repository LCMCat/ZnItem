package tech.ccat.znitem.gem

import org.bukkit.inventory.ItemStack
import tech.ccat.znitem.model.GemQuality
import tech.ccat.znitem.model.GemType
import tech.ccat.znitem.nbt.ZnItemNBT

object GemSlotManager {

    fun getGemSlots(itemStack: ItemStack): List<GemSlot> {
        val nbtSlots = ZnItemNBT.getGemSlots(itemStack)
        return nbtSlots.mapIndexed { index, data ->
            GemSlot(index, data.unlocked, data.gemType, data.gemQuality)
        }
    }

    fun setGemSlots(itemStack: ItemStack, slots: List<GemSlot>): ItemStack {
        val data = slots.map { slot ->
            ZnItemNBT.GemSlotData(slot.unlocked, slot.gemType, slot.gemQuality)
        }
        return ZnItemNBT.setGemSlots(itemStack, data)
    }

    fun unlockSlot(itemStack: ItemStack, slotIndex: Int): ItemStack {
        val slots = getGemSlots(itemStack).toMutableList()
        if (slotIndex < 0 || slotIndex >= slots.size) return itemStack
        slots[slotIndex] = slots[slotIndex].copy(unlocked = true)
        return setGemSlots(itemStack, slots)
    }

    fun insertGem(itemStack: ItemStack, slotIndex: Int, gemType: GemType, quality: GemQuality): ItemStack {
        val slots = getGemSlots(itemStack).toMutableList()
        if (slotIndex < 0 || slotIndex >= slots.size) return itemStack
        val slot = slots[slotIndex]
        if (!slot.unlocked || slot.hasGem) return itemStack
        slots[slotIndex] = slot.copy(gemType = gemType, gemQuality = quality)
        return setGemSlots(itemStack, slots)
    }

    fun removeGem(itemStack: ItemStack, slotIndex: Int): ItemStack {
        val slots = getGemSlots(itemStack).toMutableList()
        if (slotIndex < 0 || slotIndex >= slots.size) return itemStack
        val slot = slots[slotIndex]
        if (!slot.hasGem) return itemStack
        slots[slotIndex] = slot.copy(gemType = null, gemQuality = null)
        return setGemSlots(itemStack, slots)
    }
}
