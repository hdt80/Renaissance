package net.hungerstruck.renaissance.event.match

import net.hungerstruck.renaissance.match.RMatch
import org.bukkit.event.HandlerList

/**
 * Occurs when a RMatch has started, meaning players can now move around and play
 *
 * @param match RMatch that was started
 */
class RMatchStartEvent(match: RMatch) : StruckMatchEvent(match) {

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