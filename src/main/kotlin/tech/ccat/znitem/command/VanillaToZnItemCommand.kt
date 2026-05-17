package tech.ccat.znitem.command

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import tech.ccat.znitem.ZnItem
import tech.ccat.znitem.model.ZnItemEnum
import tech.ccat.znitem.nbt.ZnItemNBT

class VanillaToZnItemCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.hasPermission("znitem.admin.vanillatoznitem")) {
            sender.sendMessage("§c你没有权限执行此命令")
            return true
        }

        if (sender !is Player) {
            sender.sendMessage("§c只有玩家可以执行此命令")
            return true
        }

        val item = sender.inventory.itemInMainHand
        if (item.type.isAir) {
            sender.sendMessage("§c你手中没有物品")
            return true
        }

        if (ZnItemNBT.isZnItem(item)) {
            sender.sendMessage("§c该物品已经是ZnItem")
            return true
        }

        ZnItemNBT.markAsZnItem(item)
        ZnItemNBT.setItemId(item, ZnItemEnum.entries.first().name)
        ZnItemNBT.setUniqueId(item, java.util.UUID.randomUUID())
        ZnItemNBT.setHotPowerBooks(item, 0)
        ZnItemNBT.setReforgeType(item, tech.ccat.znitem.model.ReforgeType.NONE)
        ZnItemNBT.setRefactored(item, false)
        ZnItemNBT.setEnchants(item, emptyMap())
        ZnItemNBT.setGemSlots(item, emptyList())

        sender.sendMessage("§a已将物品转化为ZnItem")
        return true
    }
}
