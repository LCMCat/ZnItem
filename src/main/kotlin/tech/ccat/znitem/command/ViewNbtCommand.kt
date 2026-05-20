package tech.ccat.znitem.command

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType

class ViewNbtCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.hasPermission("znitem.admin.viewnbt")) {
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

        sender.sendMessage("§6========== 物品 NBT 数据 ==========")
        sender.sendMessage("§e物品类型: §f${item.type.name}")
        sender.sendMessage("§e显示名称: §f${meta.displayName()?.toString() ?: "无"}")
        val damageMeta = meta as? org.bukkit.inventory.meta.Damageable
        val durability = if (meta.isUnbreakable) {
            "不可破坏"
        } else if (damageMeta != null) {
            "${item.type.maxDurability - damageMeta.damage}/${item.type.maxDurability}"
        } else {
            "${item.type.maxDurability}/${item.type.maxDurability}"
        }
        sender.sendMessage("§e耐久度: §f$durability")
        
        val pdc = meta.persistentDataContainer
        val keys = pdc.keys
        
        if (keys.isEmpty()) {
            sender.sendMessage("§ePersistentDataContainer: §f空")
        } else {
            sender.sendMessage("§ePersistentDataContainer:")
            for (key in keys) {
                val namespace = key.namespace
                val keyStr = key.key
                
                var value: Any? = null
                try { value = pdc.get(key, PersistentDataType.BYTE) } catch (_: Exception) {}
                if (value == null) try { value = pdc.get(key, PersistentDataType.SHORT) } catch (_: Exception) {}
                if (value == null) try { value = pdc.get(key, PersistentDataType.INTEGER) } catch (_: Exception) {}
                if (value == null) try { value = pdc.get(key, PersistentDataType.LONG) } catch (_: Exception) {}
                if (value == null) try { value = pdc.get(key, PersistentDataType.FLOAT) } catch (_: Exception) {}
                if (value == null) try { value = pdc.get(key, PersistentDataType.DOUBLE) } catch (_: Exception) {}
                if (value == null) try { value = pdc.get(key, PersistentDataType.STRING) } catch (_: Exception) {}
                if (value == null) try { value = pdc.get(key, PersistentDataType.BOOLEAN) } catch (_: Exception) {}
                
                val displayValue = value?.toString() ?: "§7[复杂类型]"
                sender.sendMessage("  §7- §b$namespace:$keyStr §f= §a$displayValue")
            }
        }

        val enchants = meta.enchants
        if (enchants.isNotEmpty()) {
            sender.sendMessage("§e原版附魔:")
            enchants.forEach { (enchant, level) ->
                sender.sendMessage("  §7- §d${enchant.key.key} §f= §a$level")
            }
        }

        val lore = meta.lore
        if (!lore.isNullOrEmpty()) {
            sender.sendMessage("§eLore:")
            lore.forEach { line ->
                sender.sendMessage("  §7- §f${line.toString()}")
            }
        }

        sender.sendMessage("§6===================================")
        return true
    }
}
