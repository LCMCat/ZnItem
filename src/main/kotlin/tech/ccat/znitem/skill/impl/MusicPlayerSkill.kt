package tech.ccat.znitem.skill.impl

import org.bukkit.entity.Player
import tech.ccat.znitem.model.SkillTriggerType
import tech.ccat.znitem.skill.ItemSkill
import tech.ccat.znitem.util.music.*

class MusicPlayerSkill(
    private val songResourceName: String,
    private val fadeOutTicks: Int = 20
) : ItemSkill(
    id = "MUSIC_PLAYER",
    name = "音乐播放",
    description = listOf("§d穿戴时播放神秘的音乐"),
    manaCost = 0.0,
    cooldownSeconds = 0,
    triggerType = SkillTriggerType.EQUIP
) {
    
    private val activePlayers = mutableMapOf<String, RadioSongPlayer>()
    
    override fun execute(player: Player) {
        startPlaying(player)
    }
    
    fun startPlaying(player: Player) {
        if (activePlayers.containsKey(player.name)) return
        
        val song = Song.load(songResourceName) ?: return
        
        val songPlayer = RadioSongPlayer(song)
        songPlayer.addPlayer(player)
        songPlayer.loop = true
        songPlayer.volume = 100
        songPlayer.setPlaying(true)
        
        activePlayers[player.name] = songPlayer
    }
    
    fun stopPlaying(player: Player) {
        val songPlayer = activePlayers.remove(player.name) ?: return
        
        songPlayer.fadeStart = songPlayer.volume
        songPlayer.fadeTarget = 0
        songPlayer.fadeDuration = fadeOutTicks
        songPlayer.fadeType = FadeType.LINEAR
        songPlayer.autoDestroy = true
        
        org.bukkit.Bukkit.getScheduler().runTaskLater(
            tech.ccat.znitem.ZnItem.instance,
            Runnable {
                songPlayer.destroy()
                activePlayers.remove(player.name)
            },
            (fadeOutTicks + 10).toLong()
        )
    }
    
    fun isPlaying(player: Player): Boolean = activePlayers.containsKey(player.name)
    
    companion object {
        private val instances = mutableMapOf<String, MusicPlayerSkill>()
        
        fun getInstance(songResourceName: String, fadeOutTicks: Int = 20): MusicPlayerSkill {
            return instances.getOrPut("$songResourceName:$fadeOutTicks") {
                MusicPlayerSkill(songResourceName, fadeOutTicks)
            }
        }
    }
}
