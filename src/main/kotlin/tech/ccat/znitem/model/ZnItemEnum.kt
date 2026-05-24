package tech.ccat.znitem.model

import tech.ccat.znitem.item.AbstractZnItem
import tech.ccat.znitem.item.sword.AlphaSword
import tech.ccat.znitem.item.memento.CreativeMind
import tech.ccat.znitem.item.helmet.WildernessHelmet
import tech.ccat.znitem.item.helmet.BigBrain
import tech.ccat.znitem.item.elytra.AngelicFeather

enum class ZnItemEnum {
    ALPHA_SWORD,
    CREATIVE_MIND,
    WILDERNESS_HELMET,
    BIG_BRAIN,
    ANGELIC_FEATHER;

    fun createItem(): AbstractZnItem = when (this) {
        ALPHA_SWORD -> AlphaSword()
        CREATIVE_MIND -> CreativeMind.create()
        WILDERNESS_HELMET -> WildernessHelmet()
        BIG_BRAIN -> BigBrain()
        ANGELIC_FEATHER -> AngelicFeather()
    }

    companion object {
        fun fromId(id: String): ZnItemEnum? = entries.firstOrNull { it.name == id }
    }
}
