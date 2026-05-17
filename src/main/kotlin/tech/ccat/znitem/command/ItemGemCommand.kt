package tech.ccat.znitem.command

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import tech.ccat.znitem.ZnItem
import tech.ccat.znitem.gem.GemSlotManager
import tech.ccat.znitem.model.GemQuality
import tech.ccat.znitem.model.GemType
import tech.ccat.znitem.model.ZnItemEnum
import tech.ccat.znitem.nbt.ZnItemNBT

class ItemGemCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.hasPermission("znitem.admin.gem")) {
            sender.sendMessage("§c你没有权限执行此命令")
            return true
        }

        if (sender !is Player) {
            sender.sendMessage("§c只有玩家可以执行此命令")
            return true
        }

        if (args.isEmpty()) {
            sendUsage(sender)
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

        when (args[0].lowercase()) {
            "unlock" -> {
                if (args.size < 2) {
                    sender.sendMessage("§c用法: /itemgem unlock <槽位索引>")
                    return true
                }
                val slotIndex = args[1].toIntOrNull() ?: run { sender.sendMessage("§c无效的槽位索引"); return true }
                GemSlotManager.unlockSlot(item, slotIndex)
                sender.sendMessage("§a已解锁槽位 $slotIndex")
            }
            "insert" -> {
                if (args.size < 4) {
                    sender.sendMessage("§c用法: /itemgem insert <槽位索引> <宝石类型> <宝石品质>")
                    return true
                }
                val slotIndex = args[1].toIntOrNull() ?: run { sender.sendMessage("§c无效的槽位索引"); return true }
                val gemType = try { GemType.valueOf(args[2].uppercase()) } catch (_: Exception) { null }
                    ?: run { sender.sendMessage("§c未知的宝石类型"); return true }
                val gemQuality = try { GemQuality.valueOf(args[3].uppercase()) } catch (_: Exception) { null }
                    ?: run { sender.sendMessage("§c未知的宝石品质"); return true }
                GemSlotManager.insertGem(item, slotIndex, gemType, gemQuality)
                sender.sendMessage("§a已在槽位 $slotIndex 镶嵌 ${gemType.displayName}(${gemQuality.displayName})")
            }
            "remove" -> {
                if (args.size < 2) {
                    sender.sendMessage("§c用法: /itemgem remove <槽位索引>")
                    return true
                }
                val slotIndex = args[1].toIntOrNull() ?: run { sender.sendMessage("§c无效的槽位索引"); return true }
                GemSlotManager.removeGem(item, slotIndex)
                sender.sendMessage("§a已移除槽位 $slotIndex 的宝石")
            }
            else -> sendUsage(sender)
        }

        val itemId = ZnItemNBT.getItemId(item) ?: return true
        val znItemEnum = ZnItemEnum.fromId(itemId) ?: return true
        val znItem = znItemEnum.createItem()
        znItem.updateItemMeta(item)

        return true
    }

    private fun sendUsage(sender: CommandSender) {
        sender.sendMessage("§c用法:")
        sender.sendMessage("§c/itemgem unlock <槽位索引>")
        sender.sendMessage("§c/itemgem insert <槽位索引> <宝石类型> <宝石品质>")
        sender.sendMessage("§c/itemgem remove <槽位索引>")
        sender.sendMessage("§7宝石类型: RUBY, AMETHYST, SAPPHIRE, TOURMALINE, AGATE")
        sender.sendMessage("§7宝石品质: ROUGH, FLAWED, FINE, FLAWLESS, PERFECT")
    }
}
