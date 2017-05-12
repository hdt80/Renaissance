package net.hungerstruck.renaissance.event.player

import net.hungerstruck.renaissance.RPlayer
import org.bukkit.event.HandlerList

/**
 * Occurs when a RPlayer's sanity is changed
 *
 * @param player RPlayer whose sanity changed
 * @param sanity New sanity of the player
 */
class RPlayerSanityUpdateEvent(player: RPlayer, val sanity: Int) : RPlayerEvent(player) {

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