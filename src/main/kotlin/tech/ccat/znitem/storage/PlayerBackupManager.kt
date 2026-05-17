package tech.ccat.znitem.storage

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import tech.ccat.znitem.ZnItem
import tech.ccat.znitem.nbt.ZnItemNBT
import java.io.ByteArrayOutputStream
import java.util.UUID

class PlayerBackupManager(private val backupDao: PlayerBackupDao) {

    fun backupPlayer(player: Player, reason: String): Int {
        val items = mutableListOf<ItemStack>()
        player.inventory.contents.filterNotNull().forEach { items.add(it) }
        player.inventory.armorContents.filterNotNull().forEach { items.add(it) }
        if (player.inventory.itemInOffHand.type != org.bukkit.Material.AIR) {
            items.add(player.inventory.itemInOffHand)
        }

        val data = serializeItems(items)
        return backupDao.saveBackup(player.uniqueId, data, reason)
    }

    fun rollbackPlayer(player: Player, backupId: Int): Boolean {
        val data = backupDao.getBackupById(backupId) ?: return false
        val items = deserializeItems(data) ?: return false

        Bukkit.getScheduler().runTask(ZnItem.instance, Runnable {
            player.inventory.clear()
            var index = 0
            for (item in items) {
                if (index < 36) {
                    player.inventory.setItem(index, item)
                }
                index++
            }
        })
        return true
    }

    private fun serializeItems(items: List<ItemStack>): ByteArray {
        val baos = ByteArrayOutputStream()
        val dataList = items.mapNotNull { item ->
            try {
                val bytes = item.serializeAsBytes()
                bytes
            } catch (_: Exception) {
                null
            }
        }
        val dos = java.io.DataOutputStream(baos)
        dos.writeInt(dataList.size)
        for (bytes in dataList) {
            dos.writeInt(bytes.size)
            dos.write(bytes)
        }
        dos.flush()
        return baos.toByteArray()
    }

    private fun deserializeItems(data: ByteArray): List<ItemStack>? {
        return try {
            val bais = java.io.ByteArrayInputStream(data)
            val dis = java.io.DataInputStream(bais)
            val count = dis.readInt()
            val items = mutableListOf<ItemStack>()
            repeat(count) {
                val size = dis.readInt()
                val bytes = ByteArray(size)
                dis.readFully(bytes)
                items.add(ItemStack.deserializeBytes(bytes))
            }
            items
        } catch (_: Exception) {
            null
        }
    }
}
