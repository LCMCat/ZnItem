package tech.ccat.znitem.data

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import tech.ccat.znitem.ZnItem
import tech.ccat.znitem.item.AbstractZnItem
import tech.ccat.znitem.model.ZnItemEnum
import tech.ccat.znitem.nbt.ZnItemNBT

data class PlayerItemData(
    val playerUuid: java.util.UUID,
    val items: Map<Int, AbstractZnItem>,
    val itemStacks: Map<Int, ItemStack>
)

class PlayerDataLoader {

    fun loadPlayer(player: Player): PlayerItemData {
        val items = mutableMapOf<Int, AbstractZnItem>()
        val itemStacks = mutableMapOf<Int, ItemStack>()

        val allSlots = getAllInventorySlots(player)
        for ((slot, itemStack) in allSlots) {
            if (itemStack != null && ZnItemNBT.isZnItem(itemStack)) {
                val enumId = ZnItemNBT.getItemId(itemStack)
                if (enumId != null) {
                    val znItemEnum = ZnItemEnum.fromId(enumId)
                    if (znItemEnum != null) {
                        val znItem = znItemEnum.createItem()
                        items[slot] = znItem
                        itemStacks[slot] = itemStack
                    }
                }
            }
        }

        return PlayerItemData(player.uniqueId, items, itemStacks)
    }

    private fun getAllInventorySlots(player: Player): Map<Int, ItemStack?> {
        val slots = mutableMapOf<Int, ItemStack?>()

        player.inventory.contents.forEachIndexed { index, item ->
            if (item != null) slots[index] = item
        }

        val offHand = player.inventory.itemInOffHand
        if (offHand.type != org.bukkit.Material.AIR) {
            slots[40] = offHand
        }

        return slots
    }
}
