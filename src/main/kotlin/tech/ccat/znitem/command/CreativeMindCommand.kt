package tech.ccat.znitem.command

import net.luckperms.api.LuckPerms
import net.luckperms.api.LuckPermsProvider
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import tech.ccat.znitem.ZnItem
import tech.ccat.znitem.item.memento.CreativeMind

class CreativeMindCommand : CommandExecutor {

    private var luckPerms: LuckPerms? = null
    
    init {
        try {
            luckPerms = LuckPermsProvider.get()
        } catch (_: Exception) {
            ZnItem.instance.logger.warning("未找到 LuckPerms，玩家前缀将不显示")
        }
    }
    
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.hasPermission("znitem.admin.creativemind")) {
            sender.sendMessage("§c你没有这个权限!")
            return true
        }
        
        if (sender !is Player) {
            sender.sendMessage("§4该命令只能玩家执行!")
            return true
        }
        
        if (args.isEmpty()) {
            sender.sendMessage("§c用法: /creativemind <玩家名> <贡献>")
            return true
        }
        
        val toPlayer = Bukkit.getPlayer(args[0])
        if (toPlayer == null) {
            sender.sendMessage("§c玩家 ${args[0]} 不在线!")
            return true
        }
        
        val contribution = if (args.size > 1) {
            args.drop(1).joinToString(" ").replace("&","§")
        } else {
            "无"
        }
        
        val fromPrefix = getPrefix(sender)
        val toPrefix = getPrefix(toPlayer)
        
        val edition = ZnItem.instance.mementoEditionManager.incrementCreativeMind()
        
        val creativeMind = CreativeMind.create(
            fromName = sender.name,
            fromPrefix = fromPrefix,
            toName = toPlayer.name,
            toPrefix = toPrefix,
            contribution = contribution,
            edition = edition
        )
        
        val itemStack = creativeMind.getItemStack()
        toPlayer.inventory.addItem(itemStack)
        
        sender.sendMessage("§e物品已发放!")
        
        val fromDisplay = if (fromPrefix != null) "$fromPrefix ${sender.name}" else sender.name
        toPlayer.sendMessage("$fromDisplay §c赠送了你 §c创造之画§8(#$edition)§c!")
        
        return true
    }
    
    private fun getPrefix(player: Player): String? {
        val lp = luckPerms ?: return null
        val user = lp.userManager.getUser(player.uniqueId) ?: return null
        val raw = user.cachedData.metaData.prefix ?: return null
        return raw.replace("&", "§")
    }
}
