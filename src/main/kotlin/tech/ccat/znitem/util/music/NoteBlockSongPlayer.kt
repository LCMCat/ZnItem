package tech.ccat.znitem.util.music

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player

class NoteBlockSongPlayer(song: Song) : SongPlayer(song) {
    
    var noteBlock: Block? = null
    
    override fun playTick(player: Player, tick: Int) {
        val block = noteBlock ?: return
        
        if (!isValidNoteBlock(block)) return
        if (!isSameWorld(player, block)) return
        
        val playerVolume = NoteBlockMusicManager.getPlayerVolume(player)
        
        song.layers.values.forEach { layer ->
            val note = layer.getNote(tick) ?: return@forEach
            
            val bukkitInstrument = InstrumentMapper.toBukkitInstrument(note.instrument)
            val sound = InstrumentMapper.toSound(note.instrument)
            val noteKey = note.key - 33
            val pitch = NotePitch.getPitch(noteKey)
            val volume = calculateVolume(layer.volume, playerVolume)
            
            player.playNote(block.location, bukkitInstrument, org.bukkit.Note(noteKey))
            player.playSound(block.location, sound, volume, pitch)
        }
    }
    
    private fun isValidNoteBlock(block: Block): Boolean {
        return block.type == Material.NOTE_BLOCK
    }
    
    private fun isSameWorld(player: Player, block: Block): Boolean {
        return player.world.name == block.world.name
    }
    
    private fun calculateVolume(layerVolume: Byte, playerVolume: Byte): Float {
        return (layerVolume.toInt() * volume.toInt() * playerVolume.toInt()) / 1_000_000f
    }
}
