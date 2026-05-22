package tech.ccat.znitem.util.music

import org.bukkit.entity.Player

object NoteBlockMusicManager {
    
    private val playingSongs = mutableMapOf<String, MutableList<SongPlayer>>()
    private val playerVolumes = mutableMapOf<String, Byte>()
    
    private const val DEFAULT_VOLUME: Byte = 100
    
    fun isReceivingSong(player: Player): Boolean {
        val songs = playingSongs[player.name] ?: return false
        return songs.isNotEmpty()
    }
    
    fun stopPlaying(player: Player) {
        playingSongs[player.name]?.forEach { songPlayer ->
            songPlayer.removePlayer(player)
        }
    }
    
    fun setPlayerVolume(player: Player, volume: Byte) {
        playerVolumes[player.name] = volume
    }
    
    fun getPlayerVolume(player: Player): Byte {
        return playerVolumes.getOrPut(player.name) { DEFAULT_VOLUME }
    }
    
    fun registerPlayerSong(playerName: String, songPlayer: SongPlayer) {
        playingSongs.getOrPut(playerName) { mutableListOf() }.add(songPlayer)
    }
    
    fun unregisterPlayerSong(playerName: String, songPlayer: SongPlayer) {
        playingSongs[playerName]?.remove(songPlayer)
    }
    
    fun getPlayingSongs(player: Player): List<SongPlayer> {
        return playingSongs[player.name]?.toList() ?: emptyList()
    }
    
    fun stopAllSongs() {
        playingSongs.values.flatten().forEach { songPlayer ->
            songPlayer.destroy()
        }
        playingSongs.clear()
    }
    
    fun clearPlayerData(player: Player) {
        playingSongs.remove(player.name)
        playerVolumes.remove(player.name)
    }
}
