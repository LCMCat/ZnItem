package tech.ccat.znitem.util

import org.bukkit.entity.Player
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class CooldownManager {

    private val cooldowns = ConcurrentHashMap<String, Long>()

    fun setCooldown(player: Player, skillId: String, cooldownSeconds: Long) {
        val key = "${player.uniqueId}:$skillId"
        cooldowns[key] = System.currentTimeMillis() + cooldownSeconds * 1000
    }

    fun isOnCooldown(player: Player, skillId: String): Boolean {
        val key = "${player.uniqueId}:$skillId"
        val endTime = cooldowns[key] ?: return false
        return System.currentTimeMillis() < endTime
    }

    fun getRemainingCooldown(player: Player, skillId: String): Long {
        val key = "${player.uniqueId}:$skillId"
        val endTime = cooldowns[key] ?: return 0
        val remaining = endTime - System.currentTimeMillis()
        return if (remaining > 0) remaining / 1000 else 0
    }

    fun formatCooldown(seconds: Long): String {
        return when {
            seconds < 60 -> "${seconds}秒"
            seconds < 3600 -> "${seconds / 60}分 ${seconds % 60}秒"
            seconds < 86400 -> "${seconds / 3600}时 ${(seconds % 3600) / 60}分"
            else -> "${seconds / 86400}天 ${(seconds % 86400) / 3600}时"
        }
    }

    fun formatCooldownMillis(millis: Long): String {
        val seconds = millis / 1000
        return formatCooldown(seconds)
    }
}
