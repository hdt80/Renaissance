package net.hungerstruck.renaissance.match

import net.hungerstruck.renaissance.Renaissance
import org.bukkit.scheduler.BukkitRunnable

/**
 * Runnable used to unload a match from the server. This is done as the match isn't done unloading as soon as everyone
 *      is sent back to the lobby
 *
 * @param match RMatch to be unloaded
 */
class RMatchDeletionRunnable(var match: RMatch) : BukkitRunnable() {
    override fun run() {
        Renaissance.matchManager.unloadMatch(match)
    }

}
