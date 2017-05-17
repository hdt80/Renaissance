package net.hungerstruck.renaissance.match

import net.hungerstruck.renaissance.RLogger
import net.hungerstruck.renaissance.RPlayer
import net.hungerstruck.renaissance.Renaissance
import net.hungerstruck.renaissance.commands.CommandUtils
import net.hungerstruck.renaissance.config.RConfig
import net.hungerstruck.renaissance.event.match.RMatchEndEvent
import net.hungerstruck.renaissance.event.match.RMatchLoadEvent
import net.hungerstruck.renaissance.event.match.RMatchStartEvent
import net.hungerstruck.renaissance.util.TitleUtil
import net.hungerstruck.renaissance.spec.mapspec.RMap
import net.hungerstruck.renaissance.spec.module.RModuleContext
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.World

/**
 * Represents a match
 */
class RMatch(val id: Int, val map: RMap, val world: World) {

    var state: State = State.LOADED

    val moduleContext: RModuleContext = RModuleContext(this)

    val players: List<RPlayer>
        get() = RPlayer.getPlayers { it.match == this }

    val alivePlayers: List<RPlayer>
        get() = RPlayer.getPlayers { it.match == this && it.state == RPlayer.State.PARTICIPATING }

    val shouldEnd: Boolean
        get() = alivePlayers.size <= 1

    init {
        Bukkit.getPluginManager().callEvent(RMatchLoadEvent(this))
        RPlayer.updateVisibility()
    }

    fun sendMessage(msg: String, f: (RPlayer) -> Boolean = { true }) {
        RLogger.info("[match-$id] $msg")
        players.filter(f).forEach { it.sendMessage(RConfig.General.mainMessagePrefix + msg) }
    }

    fun sendPrefixlessMessage(msg: String, f: (RPlayer) -> Boolean = { true }) {
        RLogger.info("[match-$id] $msg")
        players.filter(f).forEach { it.sendMessage(msg) }
    }

    fun sendTitle(title: String, subtitle: String, fadeIn: Int, stay: Int, fadeOut: Int, f: (RPlayer) -> Boolean = { true }) {
        RLogger.info("[match-$id] $title $subtitle")
        players.filter(f).forEach { TitleUtil.sendTitle(it, title, subtitle, fadeIn, stay, fadeOut) }
    }

    /**
     * Begins the starting countdown for this match.
     */
    fun beginCountdown() {
        assert(state == State.LOADED, { "Cannot begin countdown from state $state" })
        state = State.STARTING
        Renaissance.countdownManager.start(RMatchStartCountdown(this), RConfig.Match.countdownTime)
    }

    /**
     * Starts the match.
     */
    fun startMatch() {
        state = State.PLAYING

        Bukkit.getPluginManager().callEvent(RMatchStartEvent(this))
        RPlayer.updateVisibility()
        sendMapInfo()

        if (shouldEnd) {
            if (alivePlayers.size == 1) {
                announceWinner(alivePlayers[0])
            } else {
                sendMessage("${ChatColor.RED}No players are playing! Ending the game.")
            }
            endMatch()
        }
    }

    /**
     * Sends map info to players
     */
    private fun sendMapInfo() {
        // Name and version
        sendPrefixlessMessage(CommandUtils.formatHeader(ChatColor.GOLD.toString() + map.mapInfo.name + " " + ChatColor.GRAY.toString() + map.mapInfo.version, ChatColor.YELLOW))

        // Objective
        sendPrefixlessMessage(ChatColor.YELLOW.toString() + map.mapInfo.objective)

        // Author(s)
        sendPrefixlessMessage(ChatColor.YELLOW.toString() + "Author" +
                (if (map.mapInfo.authors.count() > 1) {"s"} else {""}) + ": " +
                map.mapInfo.authors.map { ChatColor.GOLD.toString() + it.name }.joinToString(", "))

        // Contributors
        if(map.mapInfo.contributors.count() > 0) {
            sendPrefixlessMessage(ChatColor.YELLOW.toString() +
                    "Contributor" + (if (map.mapInfo.contributors.count() > 1) {"s"} else {""}) + ": " +
                    map.mapInfo.contributors.map { ChatColor.GOLD.toString() + it.name }.joinToString(", "))
        }
    }

    /**
     * Ends the match, beginning the closing countdown
     */
    fun endMatch(winner: RPlayer? = null) {
        state = State.ENDED

        if (winner != null) {
            announceWinner(winner)
        }

        Bukkit.getPluginManager().callEvent(RMatchEndEvent(this, winner))

        Renaissance.countdownManager.start(RMatchEndCountdown(this), RConfig.Match.endCountdownTime)
    }

    /**
     * Remove a match from memory, teleporting all participating players back to the lobby and unloading the match
     */
    fun removeMatch() {
        for (player : RPlayer in players) {
            player.reset(true)
            Renaissance.lobbyManager.defaultLobby.join(player)
        }

        // Perform any cleanup modules might want to do
        for (module in moduleContext.modules) {
            module.cleanup()
        }

        // Update all the variables of the players
        for (participant in players) {
            participant.match = null
            participant.previousState?.restore(participant)
            participant.previousState = null
        }

        // Schedule a match deletion, as for some reason the World still thinks players are in it
        RMatchDeletionRunnable(this).runTaskLater(Renaissance.plugin, 120L)
    }

    fun announceWinner(player: RPlayer) {
        sendTitle(RConfig.Match.matchEndMessageTitle.format(player.displayName), RConfig.Match.matchEndMessageSubTitle, RConfig.Match.matchEndMessageFadeIn, RConfig.Match.matchEndMessageDuration, RConfig.Match.matchEndMessageFadeOut)

        sendPrefixlessMessage("\n")
        sendMessage("${ChatColor.DARK_PURPLE}${player.displayName}${ChatColor.WHITE} has won the game!")
        sendPrefixlessMessage("\n")

        if (player.isOnline) player.allowFlight = true
        RPlayer.updateVisibility()
    }

    enum class State {
        // Loaded. Players are not in already, they are still in the lobby for this match.
        LOADED,
        // Countdown for start is running, players are in.
        STARTING,
        // The match is currently in progress.
        PLAYING,
        // The match has ended but has not yet been unloaded. When unloaded, the RMatch gets gcd.
        ENDED;
    }

}