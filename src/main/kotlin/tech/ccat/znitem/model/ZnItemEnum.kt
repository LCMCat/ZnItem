package tech.ccat.znitem.model

import tech.ccat.znitem.item.AbstractZnItem
import tech.ccat.znitem.item.sword.AlphaSword
import tech.ccat.znitem.item.memento.CreativeMind
import tech.ccat.znitem.item.helmet.WildernessHelmet

enum class ZnItemEnum {
    ALPHA_SWORD,
    CREATIVE_MIND,
    WILDERNESS_HELMET;

    fun createItem(): AbstractZnItem = when (this) {
        ALPHA_SWORD -> AlphaSword()
        CREATIVE_MIND -> CreativeMind.create()
        WILDERNESS_HELMET -> WildernessHelmet()
    }

    companion object {
        fun fromId(id: String): ZnItemEnum? = entries.firstOrNull { it.name == id }
    }
}
