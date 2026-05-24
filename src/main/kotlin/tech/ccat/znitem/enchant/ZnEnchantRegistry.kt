package tech.ccat.znitem.enchant

object ZnEnchantRegistry {
    private val enchants = mutableMapOf<String, ZnEnchant>()

    fun register(enchant: ZnEnchant) {
        enchants[enchant.id] = enchant
    }

    fun get(id: String): ZnEnchant? = enchants[id]

    fun all(): Collection<ZnEnchant> = enchants.values

    fun setup() {
        register(SharpnessEnchant())
        register(SmiteEnchant())
        register(CriticalEnchant())
        register(LifeStealEnchant())
    }
}
