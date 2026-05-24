package tech.ccat.znitem.skill.impl

import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import tech.ccat.znitem.ZnItem
import tech.ccat.znitem.model.SkillTriggerType
import tech.ccat.znitem.skill.ItemSkill
import tech.ccat.znitem.util.CooldownManager
import java.util.*

class SeraphicDescentSkill : ItemSkill(
    id = "SERAPHIC_DESCENT",
    name = "天使降临",
    description = listOf(
        "§f鞘翅起飞时触发",
        "§f向前冲刺并获得天使守护状态"
    ),
    manaCost = 300.0,
    cooldownSeconds = 5,
    triggerType = SkillTriggerType.ELYTRA_FLIGHT
) {
    
    private val cooldownManager = CooldownManager()
    private val activeDashes = mutableSetOf<UUID>()
    private val guardianState = mutableMapOf<UUID, Long>()
    
    companion object {
        private const val DASH_DURATION = 20
        private const val DASH_SPEED = 2.0
        private const val GUARDIAN_DURATION = 100L
        private const val SLOW_FALL_DURATION = 60L
        private const val PARTICLE_RADIUS = 3.0
        private const val FEATHER_COUNT = 8
        private const val FEATHER_LIFETIME = 60L
    }
    
    override fun execute(player: Player) {
        if (activeDashes.contains(player.uniqueId)) return
        
        cooldownManager.setCooldown(player, id, cooldownSeconds.toLong())
        activeDashes.add(player.uniqueId)
        
        spawnFeathers(player)
        startDash(player)
        startParticleCircle(player)
        activateGuardianState(player)
        
        player.world.playSound(player.location, Sound.ENTITY_PHANTOM_FLAP, 1.0f, 1.5f)
    }
    
    private fun spawnFeathers(player: Player) {
        val world = player.world
        val center = player.location
        
        for (i in 0 until FEATHER_COUNT) {
            val angle = (2 * Math.PI * i) / FEATHER_COUNT
            val offsetX = 0.5 * Math.cos(angle)
            val offsetZ = 0.5 * Math.sin(angle)
            
            val dropLocation = center.clone().add(offsetX, 0.5, offsetZ)
            
            val featherItem = ItemStack(Material.FEATHER)
            val item = world.dropItemNaturally(dropLocation, featherItem)
            item.pickupDelay = Int.MAX_VALUE
            item.isInvulnerable = true
            
            Bukkit.getScheduler().runTaskLater(ZnItem.instance, Runnable {
                item.remove()
            }, FEATHER_LIFETIME)
        }
    }
    
    private fun startDash(player: Player) {
        val direction = player.location.direction.normalize()
        player.velocity = direction.multiply(DASH_SPEED)
        player.isGliding = true
        
        var ticks = 0
        object : BukkitRunnable() {
            override fun run() {
                if (!player.isOnline || ticks >= DASH_DURATION) {
                    activeDashes.remove(player.uniqueId)
                    endDash(player)
                    cancel()
                    return
                }
                ticks++
            }
        }.runTaskTimer(ZnItem.instance, 1L, 1L)
    }
    
    private fun startParticleCircle(player: Player) {
        var angle = 0.0
        val playerId = player.uniqueId
        
        object : BukkitRunnable() {
            override fun run() {
                if (!player.isOnline || !activeDashes.contains(playerId)) {
                    cancel()
                    return
                }
                
                val loc = player.location
                val y = loc.y
                
                for (i in 0 until 8) {
                    val a = angle + (2 * Math.PI * i) / 8
                    val x = loc.x + PARTICLE_RADIUS * Math.cos(a)
                    val z = loc.z + PARTICLE_RADIUS * Math.sin(a)
                    
                    val particleLoc = Location(player.world, x, y, z)
                    player.world.spawnParticle(Particle.END_ROD, particleLoc, 1, 0.0, 0.0, 0.0, 0.0)
                }
                
                angle += Math.PI / 8
            }
        }.runTaskTimer(ZnItem.instance, 0L, 1L)
    }
    
    private fun activateGuardianState(player: Player) {
        val playerId = player.uniqueId
        guardianState[playerId] = System.currentTimeMillis() + (GUARDIAN_DURATION * 50)
        
        player.addPotionEffect(PotionEffect(PotionEffectType.RESISTANCE, GUARDIAN_DURATION.toInt(), 0, false, false))
    }
    
    private fun endDash(player: Player) {
        if (player.isGliding) {
            val direction = player.location.direction.normalize()
            player.velocity = direction.multiply(1.0)
        }
        player.addPotionEffect(PotionEffect(PotionEffectType.SLOW_FALLING, SLOW_FALL_DURATION.toInt(), 0, false, true))
        
        Bukkit.getScheduler().runTaskLater(ZnItem.instance, Runnable {
            if (player.isOnline) {
                player.world.playSound(player.location, Sound.ENTITY_PHANTOM_FLAP, 0.5f, 1.0f)
            }
        }, SLOW_FALL_DURATION)
    }
    
    fun isInGuardianState(player: Player): Boolean {
        val endTime = guardianState[player.uniqueId] ?: return false
        return System.currentTimeMillis() < endTime
    }
    
    fun isDashing(player: Player): Boolean = activeDashes.contains(player.uniqueId)
}
