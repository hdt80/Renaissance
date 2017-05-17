package net.hungerstruck.renaissance.commands

import com.sk89q.minecraft.util.commands.*
import net.hungerstruck.renaissance.Renaissance
import net.hungerstruck.renaissance.rplayer
import net.hungerstruck.renaissance.spec.mapspec.RMap
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object LobbyCommands {
    @JvmStatic
    @Command(
            aliases = arrayOf("setnext"),
            desc = "Set the map for the lobby",
            usage = "<map name>",
            min = 1,
            max = 1
    )
    @CommandPermissions("renaissance.setnext")
    fun setnext(args: CommandContext, sender: CommandSender) {
        val player : Player = CommandUtils.getPlayer(sender)

        if (player.rplayer.lobby == null) {
            throw CommandException("You must be in a lobby to use this command")
        }

        val map: RMap? = Renaissance.mapContext.matchMap(args.getString(0))

        if (map == null) {
            player.sendMessage("${ChatColor.DARK_RED}Map ${ChatColor.RED}${args.getString(0)} ${ChatColor.DARK_RED}not found")
            return
        }

        if (map.isLobby) {
            player.sendMessage("${ChatColor.RED}${args.getString(0)} ${ChatColor.DARK_RED}is a lobby and cannot be set.")
            return
        }

        player.rplayer.lobby?.nextMap = Renaissance.mapContext.matchMap(args.getString(0))

        player.sendMessage("${ChatColor.DARK_AQUA}Next map: ${ChatColor.AQUA}${player.rplayer.lobby?.nextMap?.mapInfo?.name}")
    }

    @JvmStatic
    @Command(
            aliases = arrayOf("getnext"),
            desc = "Get the next map for the lobby"
    )
    @CommandPermissions("renaissance.getnext")
    fun getnext(args: CommandContext, sender: CommandSender) {
        val player : Player = CommandUtils.getPlayer(sender)

        if (player.rplayer.lobby == null) {
            player.rplayer.sendMessage("${ChatColor.RED} + You must be in a lobby to use this command")
            return
        }

        player.sendMessage("${ChatColor.DARK_AQUA}Next map: ${ChatColor.AQUA}${player.rplayer.lobby?.nextMap?.mapInfo?.name}")
    }
}