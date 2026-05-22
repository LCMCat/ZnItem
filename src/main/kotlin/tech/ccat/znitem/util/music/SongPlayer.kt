package tech.ccat.znitem.util.music

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import tech.ccat.znitem.ZnItem
import java.util.Collections

abstract class SongPlayer(protected val song: Song) {
    
    private var _playing = false
    protected var currentTick = -1
    protected val playerList = mutableListOf<String>()
    
    var loop: Boolean = false
    var autoDestroy: Boolean = false
    var volume: Byte = 100
    
    var fadeType: FadeType = FadeType.LINEAR
    var fadeStart: Byte = volume
    var fadeTarget: Byte = 100
    var fadeDuration: Int = 60
    protected var fadeProgress: Int = 0
    
    private var destroyed = false
    private var playerThread: org.bukkit.scheduler.BukkitTask? = null
    
    init {
        startPlaybackThread()
    }
    
    abstract fun playTick(player: Player, tick: Int)
    
    val isPlaying: Boolean get() = _playing
    val songData: Song get() = song
    val tick: Short get() = currentTick.toShort()
    val players: List<String> get() = Collections.unmodifiableList(playerList)
    
    fun setPlaying(shouldPlay: Boolean) {
        _playing = shouldPlay
        if (!shouldPlay) {
            Bukkit.getPluginManager().callEvent(SongStoppedEvent(this))
        }
    }
    
    fun setTick(tick: Short) {
        currentTick = tick.toInt()
    }
    
    fun addPlayer(player: Player) {
        synchronized(this) {
            val playerName = player.name
            if (playerList.contains(playerName)) return
            
            playerList.add(playerName)
            NoteBlockMusicManager.registerPlayerSong(playerName, this)
        }
    }
    
    fun removePlayer(player: Player) {
        synchronized(this) {
            val playerName = player.name
            playerList.remove(playerName)
            NoteBlockMusicManager.unregisterPlayerSong(playerName, this)
            
            if (playerList.isEmpty() && autoDestroy) {
                Bukkit.getPluginManager().callEvent(SongEndEvent(this))
                destroy()
            }
        }
    }
    
    fun destroy() {
        synchronized(this) {
            val event = SongDestroyingEvent(this)
            Bukkit.getPluginManager().callEvent(event)
            
            if (event.isCancelled) return
            
            destroyed = true
            _playing = false
            currentTick = -1
        }
    }
    
    protected fun calculateFadeVolume() {
        if (fadeProgress >= fadeDuration) return
        
        val targetVolume = Interpolator.linearInterpolate(
            doubleArrayOf(0.0, fadeStart.toDouble(), fadeDuration.toDouble(), fadeTarget.toDouble()),
            fadeProgress.toDouble()
        )
        volume = targetVolume.toInt().toByte()
        fadeProgress++
    }
    
    private fun startPlaybackThread() {
        playerThread = Bukkit.getScheduler().runTaskAsynchronously(ZnItem.instance, Runnable {
            while (!destroyed && isServerRunning()) {
                val startTime = System.currentTimeMillis()
                
                synchronized(this) {
                    if (_playing) {
                        processTick()
                    }
                }
                
                waitForNextTick(startTime)
            }
        })
    }
    
    private fun processTick() {
        calculateFadeVolume()
        currentTick++
        
        if (currentTick > song.length) {
            if (loop) {
                currentTick = 0
                return
            }
            
            _playing = false
            currentTick = -1
            Bukkit.getPluginManager().callEvent(SongEndEvent(this))
            
            if (autoDestroy) {
                destroy()
            }
            return
        }
        
        Bukkit.getOnlinePlayers().forEach { player ->
            playTick(player, currentTick)
        }
    }
    
    private fun isServerRunning(): Boolean {
        return try {
            val server = Bukkit.getServer()
            val nmsServer = server.javaClass.getMethod("getServer").invoke(server)
            nmsServer.javaClass.getMethod("isRunning").invoke(nmsServer) as Boolean
        } catch (_: Exception) {
            true
        }
    }
    
    private fun waitForNextTick(startTime: Long) {
        val elapsed = System.currentTimeMillis() - startTime
        val tickDelay = (song.delay * 50).toLong()
        val sleepTime = tickDelay - elapsed
        
        if (sleepTime > 0) {
            try {
                Thread.sleep(sleepTime)
            } catch (_: InterruptedException) {}
        }
    }
}
