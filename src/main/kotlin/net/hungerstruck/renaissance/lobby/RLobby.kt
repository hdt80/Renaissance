package net.hungerstruck.renaissance.lobby

import net.hungerstruck.renaissance.RLogger
import net.hungerstruck.renaissance.RPlayer
import net.hungerstruck.renaissance.RPlayerState
import net.hungerstruck.renaissance.Renaissance
import net.hungerstruck.renaissance.config.RConfig
import net.hungerstruck.renaissance.event.lobby.RLobbyEndEvent
import net.hungerstruck.renaissance.event.player.RPlayerJoinMatchEvent
import net.hungerstruck.renaissance.match.RMatch
import net.hungerstruck.renaissance.xml.RMap
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.ChatColor.*
import org.bukkit.GameMode
import org.bukkit.World

/**
 * RLobby is the lobby players are sent to before/after a RMatch is completed
 *
 * @param lobbyWorld: World containing the lobby
 * @param lobbyMap: RMap of the lobby
 */
class RLobby(val lobbyWorld: World, val lobbyMap: RMap) {

    var nextMap: RMap? = null       // Loaded RMap the RLobby will send players to to start the match
    var match: RMatch? = null       // RMatch the players will be a part of
    var counting: Boolean = false   // If a countdown is currently happening

    // All the players in the RLobby
    val members: Collection<RPlayer>
        get() = RPlayer.getPlayers { it.lobby == this }

    // Only members in the RLobby that will be playing in the next match
    val playingMembers: Collection<RPlayer>
        get() = members.filter { !it.isForcedSpectator }

    /**
     * Have a RPlayer join the RLobby
     *
     * @param player RPlayer that is joining the RLobby
     * @param force If the player should be forced to join the lobby, even if they are already in one
     *
     * @throws IllegalArgumentException If the player is already in a RLobby
     */
    fun join(player: RPlayer, force: Boolean = false) {
        if (player.lobby != null) {
            throw IllegalArgumentException("Player is already in a lobby")
        }

        // Update the player's variables to match that they are in the RLobby
        player.lobby = this
        player.previousState = RPlayerState.create(player)
        player.match = null
        player.reset()
        player.gameMode = GameMode.SURVIVAL

        // Teleport the player to the lobby's spawnpoint
        Bukkit.getScheduler().scheduleSyncDelayedTask(Renaissance.plugin, {
            player.teleport(lobbyMap.mapInfo.lobbyProperties!!.spawnLoc.toLocation(lobbyWorld))
        }, 1)

        updateInformation()
        sendMessage("${ChatColor.GREEN}${player.displayName} ${ChatColor.GRAY}has joined the match!")

        // If there are enough players, lobbies are set to autostart and a countdown is going
        if (playingMembers.size >= RConfig.Lobby.minimumPlayerStartCount &&
                playingMembers.size <= RConfig.Lobby.maximumPlayerStartCount &&
                RConfig.Lobby.autoStart && !counting && nextMap != null) {

            startCountdown()
        }
    }

    /**
     * Start the countdown and register the countdown class
     */
    fun startCountdown() {
        // If the countdown has already happened
        if (!Renaissance.countdownManager.hasCountdown(RLobbyEndCountdown::class.java)) {
            Renaissance.countdownManager.start(RLobbyEndCountdown(this), RConfig.Lobby.countdownTime)
            counting = true
        }
    }

    /**
     * Update the actionbar for all the players in the lobby
     */
    private fun updateInformation() {
        for (player in members) {
            player.actionBarMessage =
                    (if (lobbyMap.mapInfo.lobbyProperties!!.canTakeDamage) "${GREEN}PVP $GRAY| " else "") +
                            (if (lobbyMap.mapInfo.lobbyProperties!!.canBuild) "${GREEN}Building $GRAY| " else "") +
                            "$YELLOW${playingMembers.size}/${RConfig.Lobby.maximumPlayerStartCount} players"
        }
    }

    /**
     * Starts the match, sending each player to the match and resetting the internal variables for the next map.
     * Chances are if you're calling this you mean to call startCountdown, and this will not do any sort of countdown
     */
    fun startMatch() {
        match = Renaissance.matchManager.constructMatch(nextMap!!)

        Bukkit.getPluginManager().callEvent(RLobbyEndEvent(this))

        // Call the event for each player
        for (player in members) {
            player.lobby = null
            player.match = match
            Bukkit.getPluginManager().callEvent(RPlayerJoinMatchEvent(player, match!!))
        }

        assert(members.isEmpty(), { "Still players left in lobby after end." })

        // Reset for when the players return back to the lobby
        nextMap = null
        counting = false
        match = null
    }

    /**
     * Send a message to the players in the RLobby. This will not have the mainMessagePrefix string from RConfig
     *
     * @param msg String of the message
     */
    fun sendMessage(msg: String) {
        RLogger.info("[Lobby] $msg")
        members.forEach { it.sendMessage(RConfig.General.mainMessagePrefix + msg) }
    }

    /**
     * Send a message to the players in the RLobby. This not have the mainMessagePrefix string from RConfig
     *
     * @param msg String of the message
     */
    fun sendPrefixlessMessage(msg: String) {
        RLogger.info("[Lobby] $msg")
        members.forEach { it.sendMessage(msg) }
    }
}
