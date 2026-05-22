package tech.ccat.znitem.util.music

import java.io.File

data class Song(
    val speed: Float,
    val layers: Map<Int, Layer>,
    val songHeight: Short,
    val length: Short,
    val title: String,
    val author: String,
    val description: String,
    val path: File?
) {
    val delay: Float = 20 / speed
    
    fun copy(): Song = Song(
        speed = speed,
        layers = layers,
        songHeight = songHeight,
        length = length,
        title = title,
        author = author,
        description = description,
        path = path
    )
    
    companion object {
        fun load(resourceName: String): Song? {
            val stream = javaClass.classLoader.getResourceAsStream(resourceName)
                ?: return null
            return NBSDecoder.parse(stream)
        }
        
        fun load(file: File): Song? = NBSDecoder.parse(file)
    }
}
