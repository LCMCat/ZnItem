package tech.ccat.znitem.config

class MessageConfig(config: org.bukkit.configuration.file.YamlConfiguration) {
    val itemSummoned: String = config.getString("item-summoned", "&a已给予物品") ?: "&a已给予物品"
    val itemNotFound: String = config.getString("item-not-found", "&c未知的物品ID") ?: "&c未知的物品ID"
    val notZnItem: String = config.getString("not-znitem", "&c该物品不是ZnItem") ?: "&c该物品不是ZnItem"
    val alreadyZnItem: String = config.getString("already-znitem", "&c该物品已经是ZnItem") ?: "&c该物品已经是ZnItem"
    val noPermission: String = config.getString("no-permission", "&c你没有权限执行此命令") ?: "&c你没有权限执行此命令"
    val cooldownMessage: String = config.getString("cooldown-message", "&c物品冷却中 (%time%)") ?: "&c物品冷却中 (%time%)"
    val manaNotEnough: String = config.getString("mana-not-enough", "&c法力不足") ?: "&c法力不足"
    val itemRefactored: String = config.getString("item-refactored", "&a已重构物品") ?: "&a已重构物品"
    val itemRenamed: String = config.getString("item-renamed", "&a已将物品改名为: %name%") ?: "&a已将物品改名为: %name%"
}
