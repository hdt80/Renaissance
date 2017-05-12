package net.hungerstruck.renaissance.event.match

import net.hungerstruck.renaissance.event.StruckEvent
import net.hungerstruck.renaissance.match.RMatch

/**
 * Abstract class to represent any MatchEvent
 */
abstract class StruckMatchEvent(val match: RMatch) : StruckEvent()