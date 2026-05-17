package tech.ccat.znitem.model

data class ItemRestriction(
    val combatLevel: Int = 0,
    val caLevel: Int = 0
) {
    fun isEmpty(): Boolean = combatLevel == 0 && caLevel == 0
}
