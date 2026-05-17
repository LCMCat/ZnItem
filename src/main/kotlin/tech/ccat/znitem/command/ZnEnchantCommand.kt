package tech.ccat.znitem.command

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import tech.ccat.znitem.ZnItem
import tech.ccat.znitem.enchant.ZnEnchantRegistry
import tech.ccat.znitem.model.ZnItemEnum
import tech.ccat.znitem.nbt.ZnItemNBT

class ZnEnchantCommand : CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.hasPermission("znitem.admin.enchant")) {
            sender.sendMessage("§c你没有权限执行此命令")
            return true
        }

        if (args.size < 3) {
            sender.sendMessage("§c用法: /znench <玩家> <附魔类型> <附魔等级>")
            return true
        }

        val targetPlayer = Bukkit.getPlayer(args[0])
            ?: run { sender.sendMessage("§c玩家 ${args[0]} 不在线"); return true }

        val enchantId = args[1].uppercase()
        val enchant = ZnEnchantRegistry.get(enchantId)
        if (enchant == null) {
            sender.sendMessage("§c未知的附魔类型: $enchantId")
            return true
        }

        val level = args[2].toIntOrNull()
        if (level == null || level < 0) {
            sender.sendMessage("§c无效的附魔等级")
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

        val enchants = ZnItemNBT.getEnchants(item).toMutableMap()
        if (level == 0) {
            enchants.remove(enchantId)
        } else {
            enchants[enchantId] = level
        }
        ZnItemNBT.setEnchants(item, enchants)

        val itemId = ZnItemNBT.getItemId(item) ?: return true
        val znItemEnum = ZnItemEnum.fromId(itemId) ?: return true
        val znItem = znItemEnum.createItem()
        znItem.updateItemMeta(item)

        sender.sendMessage("§a已为 ${targetPlayer.name} 的物品附魔: ${enchant.displayName} $level")
        return true
    }

    override fun onTabComplete(
        sender: CommandSender, command: Command, alias: String, args: Array<out String>
    ): List<String> {
        return when (args.size) {
            1 -> Bukkit.getOnlinePlayers().map { it.name }
            2 -> ZnEnchantRegistry.all().map { it.id }
            3 -> listOf("1", "2", "3", "4", "5")
            else -> emptyList()
        }
    }
}
