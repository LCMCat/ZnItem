package tech.ccat.znitem.lore.trigger

import org.bukkit.event.Event
import org.bukkit.entity.Player

interface LoreUpdateTrigger {
    val triggerEvents: Set<String>
    
    fun shouldTrigger(eventName: String, event: Event, player: Player): Boolean
    
    fun getAffectedSlots(player: Player): Set<Int>
}
