package tech.ccat.znitem.command

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import tech.ccat.znitem.ZnItem
import tech.ccat.znitem.model.ZnItemEnum
import tech.ccat.znitem.nbt.ZnItemNBT

class HotPowerBookCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.hasPermission("znitem.admin.hotpowerbook")) {
            sender.sendMessage("§c你没有权限执行此命令")
            return true
        }

        if (args.size < 2) {
            sender.sendMessage("§c用法: /hotpowerbook <玩家> <数量>")
            return true
        }

        val targetPlayer = Bukkit.getPlayer(args[0])
            ?: run { sender.sendMessage("§c玩家 ${args[0]} 不在线"); return true }

        val amount = args[1].toIntOrNull()
        if (amount == null || amount < 0) {
            sender.sendMessage("§c无效的数量")
            return true
        }

        val item = targetPlayer.inventory.itemInMainHand
        if (item.type.isAir) {
            sender.sendMessage("§c目标玩家手中没有物品")
            return true
        }

        if (!ZnItemNBT.isZnItem(item)) {
            sender.sendMessage("§c该物品不是ZnItem")
            return true
        }

        ZnItemNBT.setHotPowerBooks(item, amount)

        val itemId = ZnItemNBT.getItemId(item) ?: return true
        val znItemEnum = ZnItemEnum.fromId(itemId) ?: return true
        val znItem = znItemEnum.createItem()
        znItem.updateItemMeta(item)

        sender.sendMessage("§a已将 ${targetPlayer.name} 的物品炙能之书数量设置为: $amount")
        return true
    }
}
