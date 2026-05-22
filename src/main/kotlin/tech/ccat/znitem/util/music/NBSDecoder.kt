package tech.ccat.znitem.util.music

import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

object NBSDecoder {
    
    fun parse(file: File): Song? {
        return try {
            parse(FileInputStream(file), file)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    fun parse(inputStream: InputStream): Song? = parse(inputStream, null)
    
    private fun parse(inputStream: InputStream, sourceFile: File?): Song? {
        val layers = mutableMapOf<Int, Layer>()
        
        return try {
            DataInputStream(inputStream).use { stream ->
                val header = readHeader(stream)
                readNotes(stream, layers, header.version)
                readLayerMetadata(stream, layers, header.songHeight, header.version)
                
                Song(
                    speed = header.speed,
                    layers = layers,
                    songHeight = header.songHeight,
                    length = header.length,
                    title = header.title,
                    author = header.author,
                    description = header.description,
                    path = sourceFile
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    private data class SongHeader(
        val version: Byte,
        val length: Short,
        val songHeight: Short,
        val title: String,
        val author: String,
        val description: String,
        val speed: Float
    )
    
    private fun readHeader(stream: DataInputStream): SongHeader {
        val firstShort = readShort(stream)
        
        return if (firstShort == 0.toShort()) {
            val version = stream.readByte()
            stream.readByte()
            val length = readShort(stream)
            val songHeight = readShort(stream)
            val title = readString(stream)
            val author = readString(stream)
            readString(stream)
            val description = readString(stream)
            val speed = readShort(stream) / 100f
            
            skipHeaderMetadata(stream)
            
            SongHeader(version, length, songHeight, title, author, description, speed)
        } else {
            val length = firstShort
            val songHeight = readShort(stream)
            val title = readString(stream)
            val author = readString(stream)
            readString(stream)
            val description = readString(stream)
            val speed = readShort(stream) / 100f
            
            skipHeaderMetadataOld(stream)
            
            SongHeader(0, length, songHeight, title, author, description, speed)
        }
    }
    
    private fun skipHeaderMetadata(stream: DataInputStream) {
        stream.readByte()
        stream.readByte()
        stream.readByte()
        readInt(stream)
        readInt(stream)
        readInt(stream)
        readInt(stream)
        readInt(stream)
        readStringSafe(stream)
        stream.readByte()
        stream.readByte()
        readShort(stream)
    }
    
    private fun skipHeaderMetadataOld(stream: DataInputStream) {
        stream.readByte()
        stream.readByte()
        stream.readByte()
        readInt(stream)
        readInt(stream)
        readInt(stream)
        readInt(stream)
        readInt(stream)
        readStringSafe(stream)
    }
    
    private fun readStringSafe(stream: DataInputStream) {
        try {
            val length = readInt(stream)
            if (length > 0 && length < 10000) {
                repeat(length) { stream.readByte() }
            }
        } catch (_: Exception) {}
    }
    
    private fun readNotes(stream: DataInputStream, layers: MutableMap<Int, Layer>, version: Byte) {
        var currentTick = -1
        
        while (true) {
            val tickJump = readShort(stream)
            if (tickJump == 0.toShort()) break
            
            currentTick += tickJump
            readTickNotes(stream, layers, currentTick, version)
        }
    }
    
    private fun readTickNotes(
        stream: DataInputStream,
        layers: MutableMap<Int, Layer>,
        tick: Int,
        version: Byte
    ) {
        var currentLayer = -1
        
        while (true) {
            val layerJump = readShort(stream)
            if (layerJump == 0.toShort()) break
            
            currentLayer += layerJump
            val instrument = stream.readByte()
            val key = stream.readByte()
            
            if (version >= 4) {
                stream.readByte()
                stream.readUnsignedByte()
                readShort(stream)
            }
            
            addNote(layers, currentLayer, tick, instrument, key)
        }
    }
    
    private fun addNote(
        layers: MutableMap<Int, Layer>,
        layerIndex: Int,
        tick: Int,
        instrument: Byte,
        key: Byte
    ) {
        val layer = layers.getOrPut(layerIndex) { Layer() }
        layer.setNote(tick, Note(instrument, key))
    }
    
    private fun readLayerMetadata(
        stream: DataInputStream,
        layers: MutableMap<Int, Layer>,
        songHeight: Short,
        version: Byte
    ) {
        for (layerIndex in 0 until songHeight) {
            layers[layerIndex]?.let { layer ->
                layer.name = readString(stream)
                if (version >= 4) {
                    stream.readByte()
                }
                layer.volume = stream.readByte()
                stream.readUnsignedByte()
            }
        }
    }
    
    private fun readShort(stream: DataInputStream): Short {
        val lowByte = stream.readUnsignedByte()
        val highByte = stream.readUnsignedByte()
        return (lowByte + (highByte shl 8)).toShort()
    }
    
    private fun readInt(stream: DataInputStream): Int {
        val byte1 = stream.readUnsignedByte()
        val byte2 = stream.readUnsignedByte()
        val byte3 = stream.readUnsignedByte()
        val byte4 = stream.readUnsignedByte()
        return byte1 + (byte2 shl 8) + (byte3 shl 16) + (byte4 shl 24)
    }
    
    private fun readString(stream: DataInputStream): String {
        val length = readInt(stream)
        if (length <= 0) return ""
        if (length > 10000) return ""
        
        return buildString(length) {
            repeat(length) {
                val char = stream.readByte().toInt().toChar()
                append(if (char == '\r') ' ' else char)
            }
        }
    }
}
