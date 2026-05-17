package tech.ccat.znitem.model

import tech.ccat.znitem.item.AbstractZnItem
import tech.ccat.znitem.item.sword.AlphaSword

enum class ZnItemEnum {
    ALPHA_SWORD;

    fun createItem(): AbstractZnItem = when (this) {
        ALPHA_SWORD -> AlphaSword()
    }

    companion object {
        fun fromId(id: String): ZnItemEnum? = entries.firstOrNull { it.name == id }
    }
}
