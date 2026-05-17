package tech.ccat.znitem.model

enum class ItemType(val displayName: String) {
    SWORD("剑"),
    BOW("弓"),
    AXE("斧"),
    WAND("魔杖"),
    HELMET("头盔"),
    CHESTPLATE("胸甲"),
    LEGGINGS("护腿"),
    BOOTS("靴子"),
    ACCESSORY("饰品"),
    PICKAXE("镐"),
    HOE("锄头"),
    FISHING_ROD("鱼竿"),
    DRILL("钻头"),
    SHOVEL("锹"),
    SHEARS("剪刀"),
    CONSUMABLE("消耗品"),
    MATERIAL("材料"),
    BLOCK("方块"),
    RUNE("装饰品"),
    PET("宠物"),
    GEM("宝石"),
    DEPLOYABLE("部署物"),
    ARROW("箭"),
    VANILLA("原版物品"),
    MEMENTO("纪念品"),
    OTHER("其它");

    fun isWeapon(): Boolean = this in WEAPON_TYPES
    fun isArmor(): Boolean = this in ARMOR_TYPES
    fun isTool(): Boolean = this in TOOL_TYPES

    companion object {
        val WEAPON_TYPES: Set<ItemType> = setOf(SWORD, BOW, AXE, WAND, FISHING_ROD, DRILL)
        val ARMOR_TYPES: Set<ItemType> = setOf(HELMET, CHESTPLATE, LEGGINGS, BOOTS)
        val TOOL_TYPES: Set<ItemType> = setOf(PICKAXE, HOE, SHOVEL, SHEARS)
        val ENCHANT_WEAPON_TYPES: Set<ItemType> = setOf(SWORD, AXE, DRILL)
    }
}
