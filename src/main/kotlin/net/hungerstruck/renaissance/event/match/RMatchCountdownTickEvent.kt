package net.hungerstruck.renaissance.event.match

import net.hungerstruck.renaissance.match.RMatch
import org.bukkit.event.HandlerList

/**
 * Event representing a tick of a RMatchCountdown
 *
 * @param match RMatch that the RMatchCountdown tick occured
 * @param timeLeft How many seconds are remaining till the Countdown has finished
 */
class RMatchCountdownTickEvent(match: RMatch, val timeLeft: Int) : StruckMatchEvent(match) {

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
