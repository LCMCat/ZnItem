package tech.ccat.znitem.menu

import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import tech.ccat.bemenu.MenuProvider
import tech.ccat.znitem.model.ZnItemEnum

object ItemListMenu : MenuProvider {

    private val itemCache = mutableMapOf<String, ItemStack>()

    override val id = "znitem:item_list"

    override fun title(player: Player, args: Array<out Any>): String {
        return "§8物品列表 §7[共${ZnItemEnum.entries.size}种]"
    }

    override fun rows(player: Player, args: Array<out Any>): Int {
        val count = ZnItemEnum.entries.size
        return ((count + 8) / 9).coerceIn(1, 6)
    }

    override fun onBuild(player: Player, args: Array<out Any>, inventory: Inventory) {
        ZnItemEnum.entries.forEachIndexed { index, itemEnum ->
            if (index < inventory.size) {
                inventory.setItem(index, getItemStack(itemEnum))
            }
        }
    }

    override fun onClick(player: Player, args: Array<out Any>, event: InventoryClickEvent) {
        event.isCancelled = true
        val slot = event.rawSlot
        if (slot < 0) return

        val itemEnum = ZnItemEnum.entries.getOrNull(slot) ?: return
        val itemStack = getItemStack(itemEnum).clone()
        player.inventory.addItem(itemStack)
        player.sendMessage("§a获得: ${itemEnum.createItem().baseName}")
    }

    private fun getItemStack(itemEnum: ZnItemEnum): ItemStack {
        return itemCache.getOrPut(itemEnum.name) {
            itemEnum.createItem().getItemStack()
        }
    }
}
