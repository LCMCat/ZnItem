package tech.ccat.znitem.command

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import tech.ccat.znitem.ZnItem
import tech.ccat.znitem.model.ZnItemEnum
import tech.ccat.znitem.nbt.ZnItemNBT

class RenameItemCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.hasPermission("znitem.admin.rename")) {
            sender.sendMessage("§c你没有权限执行此命令")
            return true
        }

        if (sender !is Player) {
            sender.sendMessage("§c只有玩家可以执行此命令")
            return true
        }

        if (args.isEmpty()) {
            sender.sendMessage("§c用法: /renameitem <名称>")
            return true
        }

        val item = sender.inventory.itemInMainHand
        if (item.type.isAir) {
            sender.sendMessage("§c你手中没有物品")
            return true
        }

        if (!ZnItemNBT.isZnItem(item)) {
            sender.sendMessage("§c该物品不是ZnItem")
            return true
        }

        val name = args.joinToString(" ")
        ZnItemNBT.setRename(item, name)

        val itemId = ZnItemNBT.getItemId(item) ?: return true
        val znItemEnum = ZnItemEnum.fromId(itemId) ?: return true
        val znItem = znItemEnum.createItem()
        znItem.updateItemMeta(item)

        sender.sendMessage("§a已将物品改名为: $name")
        return true
    }
}
