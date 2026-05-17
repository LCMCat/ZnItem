package tech.ccat.znitem.gem

import tech.ccat.znitem.model.GemQuality
import tech.ccat.znitem.model.GemType

data class GemSlot(
    val index: Int,
    val unlocked: Boolean,
    val gemType: GemType? = null,
    val gemQuality: GemQuality? = null
) {
    val hasGem: Boolean get() = gemType != null && gemQuality != null
}
