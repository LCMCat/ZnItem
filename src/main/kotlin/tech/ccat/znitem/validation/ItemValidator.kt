package tech.ccat.znitem.validation

import org.bukkit.inventory.ItemStack
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

        val uuid = ZnItemNBT.getUniqueId(itemStack)
        if (uuid == null) {
            errors.add("ZnItem缺少唯一UUID")
        } else {
            if (uuidRegistry.isRegistered(uuid)) {
                errors.add("ZnItem UUID重复: $uuid")
            }
        }

        return if (errors.isEmpty()) ValidationResult.ok() else ValidationResult.errors(errors)
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
