package tech.ccat.znitem.lore

interface LoreComponent {
    val priority: Int
    
    fun shouldDisplay(context: LoreContext): Boolean
    
    fun render(context: LoreContext): List<String>
}
