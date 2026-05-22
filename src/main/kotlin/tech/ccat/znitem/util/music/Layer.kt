package tech.ccat.znitem.util.music

data class Layer(
    private val notes: MutableMap<Int, Note> = mutableMapOf(),
    var volume: Byte = 100,
    var name: String = ""
) {
    fun getNote(tick: Int): Note? = notes[tick]
    
    fun setNote(tick: Int, note: Note) {
        notes[tick] = note
    }
    
    fun getAllNotes(): Map<Int, Note> = notes.toMap()
}
