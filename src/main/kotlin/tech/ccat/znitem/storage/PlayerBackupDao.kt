package tech.ccat.znitem.storage

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import tech.ccat.znitem.ZnItem
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.sql.Timestamp
import java.util.UUID

class PlayerBackupDao(private val database: H2Database) {

    fun saveBackup(playerUuid: UUID, inventoryData: ByteArray, reason: String): Int {
        val sql = "INSERT INTO player_backup (player_uuid, backup_time, inventory_data, backup_reason) VALUES (?, ?, ?, ?)"
        database.getConnection().prepareStatement(sql).use { ps ->
            ps.setString(1, playerUuid.toString())
            ps.setTimestamp(2, Timestamp(System.currentTimeMillis()))
            ps.setBytes(3, inventoryData)
            ps.setString(4, reason)
            ps.executeUpdate()
            val rs = ps.generatedKeys
            return if (rs.next()) rs.getInt(1) else -1
        }
    }

    fun getLatestBackup(playerUuid: UUID): Pair<Int, ByteArray>? {
        val sql = "SELECT id, inventory_data FROM player_backup WHERE player_uuid = ? ORDER BY backup_time DESC LIMIT 1"
        database.getConnection().prepareStatement(sql).use { ps ->
            ps.setString(1, playerUuid.toString())
            val rs = ps.executeQuery()
            return if (rs.next()) rs.getInt(1) to rs.getBytes(2) else null
        }
    }

    fun getBackupById(id: Int): ByteArray? {
        val sql = "SELECT inventory_data FROM player_backup WHERE id = ?"
        database.getConnection().prepareStatement(sql).use { ps ->
            ps.setInt(1, id)
            val rs = ps.executeQuery()
            return if (rs.next()) rs.getBytes(1) else null
        }
    }
}
