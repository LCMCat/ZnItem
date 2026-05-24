package tech.ccat.znitem.model

enum class SkillTriggerType(val displayName: String) {
    RIGHT_CLICK("右键点击"),
    LEFT_CLICK("左键点击"),
    THROW("投掷"),
    EQUIP("穿戴"),
    MAIN_HAND("放于主手"),
    OFF_HAND("放于副手"),
    BOTH_HAND("放于主和副手"),
    INVENTORY("放在背包内"),
    ELYTRA_FLIGHT("鞘翅飞行")
}
