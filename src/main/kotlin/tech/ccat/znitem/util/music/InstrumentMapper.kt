package tech.ccat.znitem.util.music

import org.bukkit.Instrument
import org.bukkit.Sound

object InstrumentMapper {
    
    fun toSound(instrumentId: Byte): Sound = when (instrumentId) {
        1.toByte() -> Sound.BLOCK_NOTE_BLOCK_BASS
        2.toByte() -> Sound.BLOCK_NOTE_BLOCK_BASEDRUM
        3.toByte() -> Sound.BLOCK_NOTE_BLOCK_SNARE
        4.toByte() -> Sound.BLOCK_NOTE_BLOCK_HAT
        5.toByte() -> Sound.BLOCK_NOTE_BLOCK_GUITAR
        6.toByte() -> Sound.BLOCK_NOTE_BLOCK_FLUTE
        7.toByte() -> Sound.BLOCK_NOTE_BLOCK_BELL
        8.toByte() -> Sound.BLOCK_NOTE_BLOCK_CHIME
        9.toByte() -> Sound.BLOCK_NOTE_BLOCK_XYLOPHONE
        10.toByte() -> Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE
        11.toByte() -> Sound.BLOCK_NOTE_BLOCK_COW_BELL
        12.toByte() -> Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO
        13.toByte() -> Sound.BLOCK_NOTE_BLOCK_BIT
        14.toByte() -> Sound.BLOCK_NOTE_BLOCK_BANJO
        15.toByte() -> Sound.BLOCK_NOTE_BLOCK_PLING
        else -> Sound.BLOCK_NOTE_BLOCK_HARP
    }
    
    fun toBukkitInstrument(instrumentId: Byte): Instrument = when (instrumentId) {
        0.toByte() -> Instrument.PIANO
        1.toByte() -> Instrument.BASS_GUITAR
        2.toByte() -> Instrument.BASS_DRUM
        3.toByte() -> Instrument.SNARE_DRUM
        4.toByte() -> Instrument.STICKS
        5.toByte() -> Instrument.GUITAR
        6.toByte() -> Instrument.FLUTE
        7.toByte() -> Instrument.BELL
        8.toByte() -> Instrument.CHIME
        9.toByte() -> Instrument.XYLOPHONE
        10.toByte() -> Instrument.IRON_XYLOPHONE
        11.toByte() -> Instrument.COW_BELL
        12.toByte() -> Instrument.DIDGERIDOO
        13.toByte() -> Instrument.BIT
        14.toByte() -> Instrument.BANJO
        15.toByte() -> Instrument.PLING
        else -> Instrument.PIANO
    }
}
