package tech.ccat.znitem.skill

object SkillRegistry {
    private val skills = mutableMapOf<String, ItemSkill>()

    fun register(skill: ItemSkill) {
        skills[skill.id] = skill
    }

    fun get(id: String): ItemSkill? = skills[id]

    fun all(): Collection<ItemSkill> = skills.values

    fun setup() {
        register(tech.ccat.znitem.skill.impl.HelloWorldSkill())
        register(tech.ccat.znitem.skill.impl.SpeedBoostSkill())
    }
}
