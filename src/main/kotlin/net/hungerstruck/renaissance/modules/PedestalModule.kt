package net.hungerstruck.renaissance.modules

import com.google.common.collect.Iterables
import net.hungerstruck.renaissance.RPlayer
import net.hungerstruck.renaissance.event.lobby.RLobbyEndEvent
import net.hungerstruck.renaissance.event.player.RPlayerJoinMatchEvent
import net.hungerstruck.renaissance.lookAt
import net.hungerstruck.renaissance.match.RMatch
import net.hungerstruck.renaissance.modules.region.BlockRegion
import net.hungerstruck.renaissance.rplayer
import net.hungerstruck.renaissance.teleportable
import net.hungerstruck.renaissance.spec.inject
import net.hungerstruck.renaissance.spec.module.RModule
import net.hungerstruck.renaissance.spec.module.RModuleContext
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.util.Vector
import java.util.*

/**
 * Parses pedestals.
 */
class PedestalModule(match: RMatch, val modCtx: RModuleContext) : RModule(match, modCtx) {
    @inject lateinit var pedestals: List<BlockRegion>
    lateinit var pedestalIt: Iterator<BlockRegion>

    override fun init() {
        pedestalIt = Iterables.cycle(ArrayList(pedestals)).iterator()
        registerEvents()
    }

    @EventHandler
    fun onPlayerJoinMatch(event: RPlayerJoinMatchEvent) {
        if (!isMatch(event.match)) return
        if (match.state != RMatch.State.STARTING || event.player.isForcedSpectator) return

        event.player.state = RPlayer.State.PARTICIPATING
        event.player.reset()
        event.player.teleport(pedestalIt.next().loc.add(Vector(0.5, 0.5, 0.5)).toLocation(match.world).teleportable)

        val boundaryCenter = modCtx.getModule<BoundaryModule>()?.center
        if(boundaryCenter != null)
            event.player.teleport(event.player.location.lookAt(boundaryCenter.toLocation(match.world)))
        else
            event.player.teleport(Vector(0,0,0).toLocation(match.world))
    }

    @EventHandler
    fun onLobbyEnd(event: RLobbyEndEvent) {
        match.beginCountdown()
    }

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        if (!isMatch(event.player)) {
            return
        }

        if (match.state == RMatch.State.STARTING && event.player.rplayer.state == RPlayer.State.PARTICIPATING) {
            // Let them move in their little 1 by 1 square
            if (event.to.blockX != event.from.blockX || event.to.blockZ != event.from.blockZ) {
                event.to = event.from
            }
        }
    }
}