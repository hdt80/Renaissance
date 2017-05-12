package net.hungerstruck.renaissance.commands

import com.sk89q.minecraft.util.commands.Command
import com.sk89q.minecraft.util.commands.CommandContext
import com.sk89q.minecraft.util.commands.CommandPermissions
import com.sk89q.minecraft.util.commands.NestedCommand
import net.hungerstruck.renaissance.Renaissance
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandException
import org.bukkit.command.CommandSender
import java.util.*

object FSpecCommands {
    @JvmStatic
    @NestedCommand(ForcedSpectatorCommands::class)
    @CommandPermissions("renaissance.admin")
    @Command(aliases = arrayOf("fspec"), desc = "Forced spectator commands.", usage = "[list/add/remove]", min = 0, max = -1)
    fun fspec(args: CommandContext, sender: CommandSender) {
        // Nuthin
    }

    object ForcedSpectatorCommands {
        @JvmStatic
        @Command(
                aliases = arrayOf("add"),
                desc = "Adds a player to the forced spectator list.",
                usage = "[player/id]",
                min = 1,
                max = 1,
                flags = "u"
        )
        fun add(args: CommandContext, sender: CommandSender) {
            val id = findUUID(args.getString(0), args.hasFlag('u'))
            Renaissance.eventManager.addForcedSpectator(id)
            sender.sendMessage("${ChatColor.GREEN}Added '${ChatColor.AQUA}'$id'${ChatColor.BLUE} to the forced spectator list.")
        }

        @JvmStatic
        @Command(
                aliases = arrayOf("remove"),
                desc = "Removes a player from the forced spectator list.",
                usage = "[player/id]",
                min = 1,
                max = 1,
                flags = "u"
        )
        fun remove(args: CommandContext, sender: CommandSender) {
            val id = findUUID(args.getString(0), args.hasFlag('u'))
            Renaissance.eventManager.removeForcedSpectator(id)
            sender.sendMessage("${ChatColor.RED}Removed '${ChatColor.AQUA}'$id'${ChatColor.BLUE} from the forced spectator list.")
        }

        @JvmStatic
        @Command(
                aliases = arrayOf("list"),
                desc = "Lists all forced spectators.",
                usage = "[player/id]",
                min = 0,
                max = 0
        )
        fun list(args: CommandContext, sender: CommandSender) {
            sender.sendMessage("${ChatColor.GOLD}Forced spectators:")
            for (id in Renaissance.eventManager.getForcedSpectators()) {
                val online = Bukkit.getPlayer(id) != null
                sender.sendMessage("${if (online) ChatColor.GREEN else ChatColor.RED}$id${if (online) " (${Bukkit.getPlayer(id).name})" else ""}")
            }
        }

        /**
         * Find the UUID from a name or the string of a UUID
         *
         * @param nameOrUUID The string containing the UUID or name. If flag is true the nameOrUUID will be treated as
         *      a UUID and converted to a UUID from a string. If flag is false then then nameOrUUID will be treated as
         *      a player's name, and a lookup will occur
         * @param flag If nameOrUUID should be treated as a player's name or UUID
         *
         * @return The matching UUID
         *
         * @throws CommandException If flag is true (Meaning nameOrUUID will be treated as a username) and the player
         *      cannot be found
         */
        private fun findUUID(nameOrUUID: String, flag: Boolean): UUID {
            if (flag) {
                return UUID.fromString(nameOrUUID)
            }
            if (Bukkit.getPlayer(nameOrUUID) != null) {
                return Bukkit.getPlayer(nameOrUUID).uniqueId
            }
            throw CommandException("Unknown player '$nameOrUUID'. Use -u to add/remove UUIDs.");
        }
    }
}
