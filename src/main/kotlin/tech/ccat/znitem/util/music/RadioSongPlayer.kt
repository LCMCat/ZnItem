package tech.ccat.znitem.util.music

import org.bukkit.entity.Player

class RadioSongPlayer(song: Song) : SongPlayer(song) {
    
    override fun playTick(player: Player, tick: Int) {
        val playerVolume = NoteBlockMusicManager.getPlayerVolume(player)
        
        song.layers.values.forEach { layer ->
            val note = layer.getNote(tick) ?: return@forEach
            
            val sound = InstrumentMapper.toSound(note.instrument)
            val pitch = NotePitch.getPitch(note.key - 33)
            val volume = calculateVolume(layer.volume, playerVolume)
            
            player.playSound(
                player.eyeLocation,
                sound,
                volume,
                pitch
            )
        }
    }
    
    private fun calculateVolume(layerVolume: Byte, playerVolume: Byte): Float {
        return (layerVolume.toInt() * volume.toInt() * playerVolume.toInt()) / 1_000_000f
    }
}
