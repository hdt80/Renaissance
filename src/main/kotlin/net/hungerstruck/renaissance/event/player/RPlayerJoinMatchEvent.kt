package net.hungerstruck.renaissance.event.player

import net.hungerstruck.renaissance.RPlayer
import net.hungerstruck.renaissance.match.RMatch
import org.bukkit.event.HandlerList

/**
 * Occurs when a RPlayer has joined a match, either in progress or not yet started
 *
 * @param player RPlayer that joined the match
 * @param match RMatch that the player joined
 */
class RPlayerJoinMatchEvent(player: RPlayer, val match: RMatch) : RPlayerEvent(player) {

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