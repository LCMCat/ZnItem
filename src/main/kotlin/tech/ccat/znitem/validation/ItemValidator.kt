package tech.ccat.znitem.validation

import org.bukkit.inventory.ItemStack
import tech.ccat.znitem.model.ZnItemEnum
import tech.ccat.znitem.nbt.ZnItemNBT

class ItemValidator(private val uuidRegistry: UUIDRegistry) {

    fun validateItem(itemStack: ItemStack): ValidationResult {
        val errors = mutableListOf<String>()

        if (!ZnItemNBT.isZnItem(itemStack)) {
            return ValidationResult.ok()
        }

        val itemId = ZnItemNBT.getItemId(itemStack)
        if (itemId == null) {
            errors.add("ZnItem缺少物品ID")
        }

        val requiresUuid = shouldRequireUuid(itemStack)
        val uuid = ZnItemNBT.getUniqueId(itemStack)
        
        if (requiresUuid && uuid == null) {
            errors.add("ZnItem缺少唯一UUID")
        } else if (uuid != null) {
            if (uuidRegistry.isRegistered(uuid)) {
                errors.add("ZnItem UUID重复: $uuid")
            }
        }

        return if (errors.isEmpty()) ValidationResult.ok() else ValidationResult.errors(errors)
    }

    private fun shouldRequireUuid(itemStack: ItemStack): Boolean {
        val itemId = ZnItemNBT.getItemId(itemStack) ?: return true
        val znItemEnum = ZnItemEnum.fromId(itemId) ?: return true
        val znItem = znItemEnum.createItem()
        return znItem.requiresUuid
    }

    fun validateAndRegister(itemStack: ItemStack, playerUuid: java.util.UUID): ValidationResult {
        val result = validateItem(itemStack)
        if (!result.valid) return result

        val uuid = ZnItemNBT.getUniqueId(itemStack)
        if (uuid != null) {
            val registered = uuidRegistry.register(uuid, playerUuid)
            if (!registered) {
                return ValidationResult.error("ZnItem UUID已被注册: $uuid")
            }
        }

        return ValidationResult.ok()
    }
}
