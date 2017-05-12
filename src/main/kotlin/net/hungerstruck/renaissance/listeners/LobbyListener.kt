package net.hungerstruck.renaissance.listeners

import net.hungerstruck.renaissance.Renaissance
import net.hungerstruck.renaissance.config.RConfig
import net.hungerstruck.renaissance.lobby.RLobby
import net.hungerstruck.renaissance.rplayer
import net.hungerstruck.renaissance.teleportable
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.entity.ItemSpawnEvent
import org.bukkit.event.player.AsyncPlayerChatEvent

/**
 * As lobbies cannot contain modules (they are designed for matches), this listener handles block breaking, damage, etc.
 */
class LobbyListener : Listener {
    //FIXME: Maybe cancel interacts (chests etc)?

    // Redirect chat to only the lobby.
    @EventHandler
    fun onChat(event: AsyncPlayerChatEvent) {
        // Ignore commands
        if (event.message.startsWith("/")) {
            return
        }

        val lobby = event.player.rplayer.lobby ?: return

        lobby.sendPrefixlessMessage(RConfig.Lobby.chatFormat.format(event.player.name, event.message))
        event.isCancelled = true
    }

    // Cancel item drops if we don't allow breaking of blocks. Otherwise, go ahead.
    @EventHandler
    fun onItemDrop(event: ItemSpawnEvent) {
        val lobby = getLobby(event.entity.world) ?: return
        event.isCancelled = !lobby.lobbyMap.mapInfo.lobbyProperties!!.canBuild
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val lobby = getLobby(event.block.world) ?: return
        event.isCancelled = !lobby.lobbyMap.mapInfo.lobbyProperties!!.canBuild
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        val lobby = getLobby(event.block.world) ?: return
        event.isCancelled = !lobby.lobbyMap.mapInfo.lobbyProperties!!.canBuild
    }

    @EventHandler
    fun onPlayerDamageEvent(event: EntityDamageEvent) {
        if (event.entity !is Player) return // We don't care about non-players
        val lobby = getLobby(event.entity.world) ?: return

        if (event is EntityDamageByEntityEvent) {
            assert(lobby == getLobby(event.damager.world), { "Somehow, these entities are not in the same lobby." })

            // Cancel any non-player damage
            if (event.damager !is Player) {
                event.isCancelled = true
                return
            }

            if (lobby.lobbyMap.mapInfo.lobbyProperties!!.canTakeDamage) {
                event.damage = 0.0
            } else {
                event.isCancelled = true
            }
        } else {
            // Always cancel any non-player damage. Teleport them to spawn if it is void damage.
            event.isCancelled = true
            if (event.cause == EntityDamageEvent.DamageCause.VOID) {
                event.entity.teleport(event.entity.world.spawnLocation)
            }
        }
    }

    @EventHandler
    fun onHungerDrain(event: FoodLevelChangeEvent) {
        val lobby = getLobby(event.entity.world) ?: return
        event.isCancelled = true
    }

    private fun getLobby(world: World): RLobby? {
        return Renaissance.lobbyManager.isLobbyWorld(world)
    }
}