package tech.ccat.znitem.storage

import org.bukkit.inventory.ItemStack
import tech.ccat.znitem.ZnItem
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Timestamp
import java.util.UUID

class H2Database {

    private var connection: Connection? = null

    fun connect() {
        Class.forName("org.h2.Driver")

        val plugin = ZnItem.instance
        val dbFile = java.io.File(plugin.dataFolder, "znitem_backup")
        val url = "jdbc:h2:${dbFile.absolutePath};AUTO_SERVER=TRUE"

        connection = DriverManager.getConnection(url, "sa", "")
        createTables()
        plugin.logger.info("H2数据库已连接")
    }

    fun disconnect() {
        connection?.close()
        connection = null
        ZnItem.instance.logger.info("H2数据库已断开")
    }

    fun getConnection(): Connection = connection ?: throw IllegalStateException("H2数据库未连接")

    private fun createTables() {
        val sql = """
            CREATE TABLE IF NOT EXISTS player_backup (
                id INT AUTO_INCREMENT PRIMARY KEY,
                player_uuid VARCHAR(36) NOT NULL,
                backup_time TIMESTAMP NOT NULL,
                inventory_data BLOB NOT NULL,
                backup_reason VARCHAR(50)
            )
        """.trimIndent()
        getConnection().createStatement().use { it.execute(sql) }
    }
}
