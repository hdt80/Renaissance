package net.hungerstruck.renaissance.event.player

import net.hungerstruck.renaissance.RPlayer
import net.hungerstruck.renaissance.event.StruckEvent

/**
 * Abstract class representing any RPlayerEvent
 *
 * @param player RPlayer that triggered the event
 */
abstract class RPlayerEvent(val player: RPlayer) : StruckEvent()