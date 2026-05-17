package tech.ccat.znitem.data

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import tech.ccat.znitem.ZnItem
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class PlayerDataManager {

    private val playerData = ConcurrentHashMap<UUID, PlayerItemData>()
    private val loader = PlayerDataLoader()

    fun loadPlayer(player: Player): PlayerItemData {
        val data = loader.loadPlayer(player)
        playerData[player.uniqueId] = data
        return data
    }

    fun unloadPlayer(player: Player) {
        playerData.remove(player.uniqueId)
    }

    fun getPlayerData(player: Player): PlayerItemData? = playerData[player.uniqueId]

    fun refreshPlayer(player: Player): PlayerItemData {
        return loadPlayer(player)
    }

    fun refreshAll() {
        Bukkit.getOnlinePlayers().forEach { refreshPlayer(it) }
    }

    fun getAllPlayerData(): Map<UUID, PlayerItemData> = playerData.toMap()
}
