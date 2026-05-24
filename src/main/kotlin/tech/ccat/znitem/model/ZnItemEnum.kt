package tech.ccat.znitem.model

import tech.ccat.znitem.item.AbstractZnItem
import tech.ccat.znitem.item.sword.*
import tech.ccat.znitem.item.memento.*
import tech.ccat.znitem.item.helmet.*
import tech.ccat.znitem.item.elytra.*
import tech.ccat.znitem.item.consumable.*

enum class ZnItemEnum {
    ALPHA_SWORD,
    CREATIVE_MIND,
    WILDERNESS_HELMET,
    BIG_BRAIN,
    ANGELIC_FEATHER,
    GLUTTONOUS_PUFFERFISH;

    fun createItem(): AbstractZnItem = when (this) {
        ALPHA_SWORD -> AlphaSword()
        CREATIVE_MIND -> CreativeMind.create()
        WILDERNESS_HELMET -> WildernessHelmet()
        BIG_BRAIN -> BigBrain()
        ANGELIC_FEATHER -> AngelicFeather()
        GLUTTONOUS_PUFFERFISH -> GluttonousPufferfish()
    }

    companion object {
        fun fromId(id: String): ZnItemEnum? = entries.firstOrNull { it.name == id }
    }
}
