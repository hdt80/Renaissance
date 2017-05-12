package net.hungerstruck.renaissance.event.player

import net.hungerstruck.renaissance.RPlayer
import org.bukkit.event.HandlerList

/**
 * Occurs when a RPlayer's thirst is changed
 *
 * @param player RPlayer whose thirst changed
 * @param thirst New thirst of the player
 */
class RPlayerThirstUpdateEvent(player: RPlayer, val thirst: Int) : RPlayerEvent(player) {

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
