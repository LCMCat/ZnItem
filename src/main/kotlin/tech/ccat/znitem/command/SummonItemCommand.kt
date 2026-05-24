package tech.ccat.znitem.command

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import tech.ccat.bemenu.BeMenu
import tech.ccat.znitem.model.ZnItemEnum

class SummonItemCommand : CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.hasPermission("znitem.admin.summon")) {
            sender.sendMessage("§c你没有权限执行此命令")
            return true
        }

        if (args.isEmpty()) {
            if (sender !is Player) {
                sender.sendMessage("§c控制台必须指定物品ID")
                return true
            }
            val beMenu = Bukkit.getServicesManager().getRegistration(BeMenu::class.java)?.provider
            if (beMenu != null && beMenu.exists("znitem:item_list")) {
                beMenu.open(sender, "znitem:item_list")
            } else {
                sender.sendMessage("§cBeMenu 未安装或菜单未注册")
            }
            return true
        }

        val targetPlayer: Player
        val itemId: String

        when (args.size) {
            1 -> {
                if (sender !is Player) {
                    sender.sendMessage("§c控制台必须指定玩家")
                    return true
                }
                targetPlayer = sender
                itemId = args[0].uppercase()
            }
            2 -> {
                targetPlayer = Bukkit.getPlayer(args[0])
                    ?: run { sender.sendMessage("§c玩家 ${args[0]} 不在线"); return true }
                itemId = args[1].uppercase()
            }
            else -> {
                sender.sendMessage("§c用法: /summonitem 或 /summonitem <物品ID> 或 /summonitem <玩家> <物品ID>")
                return true
            }
        }

        val znItemEnum = ZnItemEnum.fromId(itemId)
        if (znItemEnum == null) {
            sender.sendMessage("§c未知的物品ID: $itemId")
            return true
        }

        val znItem = znItemEnum.createItem()
        val itemStack = znItem.getItemStack()

        targetPlayer.inventory.addItem(itemStack)
        sender.sendMessage("§a已给予 ${targetPlayer.name} 物品: ${znItem.baseName}")
        return true
    }

    override fun onTabComplete(
        sender: CommandSender, command: Command, alias: String, args: Array<out String>
    ): List<String> {
        return when (args.size) {
            1 -> ZnItemEnum.entries.map { it.name }.filter { it.startsWith(args[0].uppercase()) }
            2 -> Bukkit.getOnlinePlayers().map { it.name }.filter { it.startsWith(args[1]) }
            else -> emptyList()
        }
    }
}
