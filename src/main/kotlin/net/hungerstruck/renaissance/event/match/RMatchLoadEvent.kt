package net.hungerstruck.renaissance.event.match

import net.hungerstruck.renaissance.match.RMatch
import org.bukkit.event.HandlerList

/**
 * Occurs when a RMatch is loaded. This does not mean that a RMatch has started, players are not guaranteed to have
 *      been loaded into the RMatch yet
 *
 * @param match RMatch that was loaded
 */
class RMatchLoadEvent(match: RMatch) : StruckMatchEvent(match) {

    companion object {
        val handlers = HandlerList()

        @JvmStatic fun getHandlerList(): HandlerList {
            return handlers
        }
    }

    override fun getHandlers(): HandlerList {
        return Companion.handlers
    }
}