package net.hungerstruck.renaissance.modules.ux

import net.hungerstruck.renaissance.match.RMatch
import net.hungerstruck.renaissance.settings.Settings
import net.hungerstruck.renaissance.util.ParticleBuilder
import net.hungerstruck.renaissance.xml.module.RModule
import net.hungerstruck.renaissance.xml.module.RModuleContext
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageEvent

class BloodModule(match: RMatch, modCtx: RModuleContext) : RModule(match, modCtx) {
    private lateinit var particle: ParticleBuilder

    override fun init() {
        this.particle = ParticleBuilder().setParticle(Particle.BLOCK_CRACK).setOffset(0.25f).setCount(30).setData(Material.REDSTONE_BLOCK.id)
        registerEvents()
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerHit(event: EntityDamageEvent) {
        if (event.isCancelled || !isMatch(event.entity)) return
        match.players.filter { it.getSetting<Boolean>(Settings.BLOOD_OPTIONS) == true }.forEach {
            it.bukkit.spawnParticle(particle.particle, it.bukkit.location, particle.count)
        }
    }
}
