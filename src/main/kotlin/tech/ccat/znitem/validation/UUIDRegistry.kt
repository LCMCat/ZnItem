package tech.ccat.znitem.validation

import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class UUIDRegistry {

    private val registeredUuids = ConcurrentHashMap<UUID, UUID>()

    fun register(uuid: UUID, playerUuid: UUID): Boolean {
        val existing = registeredUuids.putIfAbsent(uuid, playerUuid)
        return existing == null
    }

    fun unregister(uuid: UUID) {
        registeredUuids.remove(uuid)
    }

    fun isRegistered(uuid: UUID): Boolean = registeredUuids.containsKey(uuid)

    fun unregisterAllForPlayer(playerUuid: UUID) {
        registeredUuids.entries.removeIf { it.value == playerUuid }
    }

    fun clear() {
        registeredUuids.clear()
    }

    fun findDuplicates(): Map<UUID, List<UUID>> {
        val countMap = mutableMapOf<UUID, MutableList<UUID>>()
        registeredUuids.forEach { (itemUuid, playerUuid) ->
            countMap.getOrPut(itemUuid) { mutableListOf() }.add(playerUuid)
        }
        return countMap.filter { it.value.size > 1 }
    }
}
