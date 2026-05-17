package tech.ccat.znitem.command

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import tech.ccat.znitem.ZnItem
import tech.ccat.znitem.model.ZnItemEnum
import tech.ccat.znitem.nbt.ZnItemNBT

class RefactorItemCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.hasPermission("znitem.admin.refactor")) {
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

        if (!ZnItemNBT.isZnItem(item)) {
            sender.sendMessage("§c该物品不是ZnItem")
            return true
        }

        val alreadyRefactored = ZnItemNBT.isRefactored(item)
        if (alreadyRefactored) {
            sender.sendMessage("§c该物品已经重构过了")
            return true
        }

        ZnItemNBT.setRefactored(item, true)

        val itemId = ZnItemNBT.getItemId(item) ?: return true
        val znItemEnum = ZnItemEnum.fromId(itemId) ?: return true
        val znItem = znItemEnum.createItem()
        znItem.updateItemMeta(item)

        sender.sendMessage("§a已重构物品，稀有度提升至: ${znItem.getEffectiveRarity(item).displayName}")
        return true
    }
}
