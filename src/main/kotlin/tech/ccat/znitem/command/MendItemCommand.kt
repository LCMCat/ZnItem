package tech.ccat.znitem.command

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.Damageable

class MendItemCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.hasPermission("znitem.admin.menditem")) {
            sender.sendMessage("§c你没有权限执行此命令")
            return true
        }

        if (sender !is Player) {
            sender.sendMessage("§c此命令只能由玩家执行")
            return true
        }

        val item = sender.inventory.itemInMainHand
        if (item.type.isAir) {
            sender.sendMessage("§c你手中没有物品")
            return true
        }

        val meta = item.itemMeta
        if (meta == null) {
            sender.sendMessage("§c物品没有元数据")
            return true
        }

        val damageable = meta as? Damageable
        if (damageable == null) {
            sender.sendMessage("§c该物品没有耐久度")
            return true
        }

        if (damageable.isUnbreakable) {
            sender.sendMessage("§c该物品不可破坏，无需修复")
            return true
        }

        val maxDurability = item.type.maxDurability
        if (maxDurability <= 0) {
            sender.sendMessage("§c该物品没有耐久度")
            return true
        }

        val currentDamage = damageable.damage
        if (currentDamage <= 0) {
            sender.sendMessage("§e该物品已经是满耐久")
            return true
        }

        damageable.damage = 0
        item.itemMeta = meta

        val displayName = item.itemMeta?.displayName()
        val itemName = if (displayName != null) {
            PlainTextComponentSerializer.plainText().serialize(displayName)
        } else {
            item.type.name
        }
        sender.sendMessage("§a已修复 §f$itemName §a的耐久度")

        return true
    }
}
