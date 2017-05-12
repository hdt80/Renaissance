package net.hungerstruck.renaissance.event.lobby

import net.hungerstruck.renaissance.event.StruckEvent
import net.hungerstruck.renaissance.lobby.RLobby

/**
 * General abstract class for a LobbyEvent
 */
abstract class StruckLobbyEvent(val lobby: RLobby) : StruckEvent()