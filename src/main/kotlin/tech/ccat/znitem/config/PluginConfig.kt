package tech.ccat.znitem.config

import org.bukkit.configuration.ConfigurationSection

class PluginConfig(config: org.bukkit.configuration.file.YamlConfiguration) {
    val autoBackupInterval: Int = config.getInt("auto-backup-interval", 5)
    val maxBackupsPerPlayer: Int = config.getInt("max-backups-per-player", 10)
    val duplicateItemAction: String = config.getString("duplicate-item-action", "WARN") ?: "WARN"
}
