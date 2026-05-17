package tech.ccat.znitem.reforge

import tech.ccat.znitem.model.ReforgeType

object ReforgeRegistry {
    private val reforges = mutableMapOf<ReforgeType, Reforge>()

    fun register(reforge: Reforge) {
        reforges[reforge.reforgeType] = reforge
    }

    fun get(type: ReforgeType): Reforge? = reforges[type]

    fun all(): Collection<Reforge> = reforges.values

    fun setup() {
        register(tech.ccat.znitem.reforge.sword.InfernalReforge())
        register(tech.ccat.znitem.reforge.sword.DeceptionReforge())
        register(tech.ccat.znitem.reforge.sword.WeatheredReforge())
        register(tech.ccat.znitem.reforge.bow.MechanismReforge())
        register(tech.ccat.znitem.reforge.bow.RigidReforge())
    }
}
