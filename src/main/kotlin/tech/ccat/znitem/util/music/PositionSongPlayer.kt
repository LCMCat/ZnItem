package tech.ccat.znitem.util.music

import org.bukkit.Location
import org.bukkit.entity.Player

class PositionSongPlayer(song: Song) : SongPlayer(song) {
    
    var targetLocation: Location? = null
    
    override fun playTick(player: Player, tick: Int) {
        val location = targetLocation ?: return
        
        if (!isSameWorld(player, location)) return
        
        val playerVolume = NoteBlockMusicManager.getPlayerVolume(player)
        
        song.layers.values.forEach { layer ->
            val note = layer.getNote(tick) ?: return@forEach
            
            val sound = InstrumentMapper.toSound(note.instrument)
            val pitch = NotePitch.getPitch(note.key - 33)
            val volume = calculateVolume(layer.volume, playerVolume)
            
            player.playSound(location, sound, volume, pitch)
        }
    }
    
    private fun isSameWorld(player: Player, location: Location): Boolean {
        return player.world.name == location.world?.name
    }
    
    private fun calculateVolume(layerVolume: Byte, playerVolume: Byte): Float {
        return (layerVolume.toInt() * volume.toInt() * playerVolume.toInt()) / 1_000_000f
    }
}
