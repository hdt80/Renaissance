package net.hungerstruck.renaissance.event.lobby

import net.hungerstruck.renaissance.lobby.RLobby
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/**
 * Occurs when a RLobby has ended, causing a match start
 */
class RLobbyEndEvent(lobby: RLobby) : StruckLobbyEvent(lobby) {

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

