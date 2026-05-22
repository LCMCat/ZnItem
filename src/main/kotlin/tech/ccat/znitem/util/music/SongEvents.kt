package tech.ccat.znitem.util.music

import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import tech.ccat.kstats.event.StatUpdateEvent

class SongEndEvent(val songPlayer: SongPlayer) : Event() {
    
    companion object {
        private val handlers = HandlerList()
        
        @JvmStatic
        fun getHandlerList() = handlers
    }

    override fun getHandlers(): HandlerList {
        return Companion.handlers
    }
}

class SongStoppedEvent(val songPlayer: SongPlayer) : Event() {
    
    companion object {
        private val handlers = HandlerList()
        
        @JvmStatic
        fun getHandlerList() = handlers
    }

    override fun getHandlers(): HandlerList {
        return Companion.handlers
    }
}

class SongDestroyingEvent(val songPlayer: SongPlayer) : Event(), org.bukkit.event.Cancellable {
    
    companion object {
        private val handlers = HandlerList()
        
        @JvmStatic
        fun getHandlerList() = handlers
    }
    
    private var cancelled = false

    override fun getHandlers(): HandlerList {
        return Companion.handlers
    }
    override fun isCancelled(): Boolean = cancelled
    override fun setCancelled(cancel: Boolean) {
        cancelled = cancel
    }
}
