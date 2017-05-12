package net.hungerstruck.renaissance.commands

import com.google.common.base.Strings
import org.bukkit.ChatColor
import org.bukkit.command.CommandException
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.util.ChatPaginator

object CommandUtils {
    /**
     * Gets a player from sender
     *
     * @param sender CommandSender
     * @return Player
     * @throws CommandException thrown if the sender is not a player
     */
    fun getPlayer(sender: CommandSender): Player {
        // If the sender is an in game player then return a casted sender
        if (sender is Player) return sender
        // otherwise stop the rest of the command from executing and provide a helpful message to the player
        throw CommandException("You must be a player to execute this command!")
    }

    /**
     * Format a header with the default color of ChatColor.RED
     *
     * @param title String to be formatted
     *
     * @return The formatted string
     */
    fun formatHeader(title: String): String {
        return formatHeader(title, ChatColor.RED)
    }

    /**
     * Format a header using ChatPagination
     *
     * @param title String to be formatted
     * @param paddingColor ChatColor to use with the padding dashes
     *
     * @return The padded string
     */
    fun formatHeader(title: String, paddingColor: ChatColor): String {
        val titleLen: Int = ChatColor.stripColor(title).length
        val padLen: Int = (ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH - titleLen) / 2 - 2
        val padding: String = paddingColor.toString() + ChatColor.STRIKETHROUGH + Strings.repeat("-", padLen)
        return padding + ChatColor.RESET + " " + title + " " + padding
    }
}