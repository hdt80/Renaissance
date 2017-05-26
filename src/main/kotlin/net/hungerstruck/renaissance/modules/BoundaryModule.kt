package net.hungerstruck.renaissance.modules

import net.hungerstruck.renaissance.RLogger
import net.hungerstruck.renaissance.RPlayer
import net.hungerstruck.renaissance.match.RMatch
import net.hungerstruck.renaissance.modules.region.RectangleRegion
import net.hungerstruck.renaissance.rplayer
import net.hungerstruck.renaissance.spec.inject
import net.hungerstruck.renaissance.spec.module.RModule
import net.hungerstruck.renaissance.spec.module.RModuleContext
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.util.Vector

/**
 * Boundary module.
 */
class BoundaryModule(match: RMatch, modCtx: RModuleContext) : RModule(match, modCtx) {
    @inject lateinit var center: Vector
    @inject lateinit var region: RectangleRegion

    override fun init() {
        registerEvents()

        // Update the map about where the center and region are so they are accessible outside the MapSpec
        match.map.mapBuilder.center = center
        match.map.mapBuilder.region = region
    }

    @EventHandler
    fun onMove(event: PlayerMoveEvent) {
        // If they are not in a match or a spectating
        if (!isMatch(event.player) || event.player.rplayer.state == RPlayer.State.SPECTATING) {
            return
        }

        if (match.state == RMatch.State.PLAYING) {
            if (!region.contains(event.to.toVector())) {
                event.player.vehicle?.eject()
                event.to = event.from
            }
        }
    }

    @EventHandler
    fun onTeleport(event: PlayerTeleportEvent){
        if(!isMatch(event.player)) {
            return
        }

        if (match.state == RMatch.State.PLAYING) {
            if (!region.contains(event.to.toVector())) {
                event.isCancelled = true
            }
        }
    }
}