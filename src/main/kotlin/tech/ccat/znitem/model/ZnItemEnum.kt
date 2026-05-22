package tech.ccat.znitem.model

import tech.ccat.znitem.item.AbstractZnItem
import tech.ccat.znitem.item.sword.AlphaSword
import tech.ccat.znitem.item.memento.CreativeMind

enum class ZnItemEnum {
    ALPHA_SWORD,
    CREATIVE_MIND;

    fun createItem(): AbstractZnItem = when (this) {
        ALPHA_SWORD -> AlphaSword()
        CREATIVE_MIND -> CreativeMind.create()
    }

    companion object {
        fun fromId(id: String): ZnItemEnum? = entries.firstOrNull { it.name == id }
    }
}
