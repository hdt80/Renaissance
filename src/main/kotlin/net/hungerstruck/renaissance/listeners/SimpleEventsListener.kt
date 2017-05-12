package net.hungerstruck.renaissance.listeners

import net.hungerstruck.renaissance.Renaissance
import net.hungerstruck.renaissance.match.RMatch
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.*
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.weather.WeatherChangeEvent

/**
 * Cancels world events in matches that have not started
 */
class SimpleEventsListener : Listener {
    /**
     * Cancels an event if the match state is not PLAYING (the world has been loaded but players are not in it)
     */
    private fun cancelEventIfNotStarted(event: org.bukkit.event.Cancellable, world: World) {
        if (shouldCancel(world))
            event.isCancelled = true
    }

    /**
     * Check if a world has a match existing, and if it does make sure it isn't in the PLAYING state
     *
     * @param world World to perform the check on
     *
     * @return If the event should be cancelled
     */
    private fun shouldCancel(world: World): Boolean {
        return Renaissance.matchManager.matches[world] != null &&
                Renaissance.matchManager.matches[world]!!.state != RMatch.State.PLAYING
    }

    @EventHandler
    fun onBlockRedstoneEvent(event: BlockRedstoneEvent) {
        if (shouldCancel(event.block.world))
            event.newCurrent = event.oldCurrent
    }

    @EventHandler
    fun onCreatureSpawn(event: CreatureSpawnEvent) {
        cancelEventIfNotStarted(event, event.entity.world)
    }

    @EventHandler
    fun onWeatherChange(event: WeatherChangeEvent) {
        cancelEventIfNotStarted(event, event.world)
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onEntityExplode(event: EntityExplodeEvent) {
        cancelEventIfNotStarted(event, event.location.world)
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onBurn(event: BlockBurnEvent) {
        cancelEventIfNotStarted(event, event.block.world)
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onFade(event: BlockFadeEvent) {
        cancelEventIfNotStarted(event, event.block.world)
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onForm(event: BlockFormEvent) {
        cancelEventIfNotStarted(event, event.block.world)
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onSpread(event: BlockSpreadEvent) {
        cancelEventIfNotStarted(event, event.block.world)
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onFromTo(event: BlockFromToEvent) {
        cancelEventIfNotStarted(event, event.block.world)
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onBlockIgnite(event: BlockIgniteEvent) {
        cancelEventIfNotStarted(event, event.block.world)
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onPistonExtend(event: BlockPistonExtendEvent) {
        cancelEventIfNotStarted(event, event.block.world)
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onPistonRetract(event: BlockPistonRetractEvent) {
        cancelEventIfNotStarted(event, event.block.world)
    }
}
